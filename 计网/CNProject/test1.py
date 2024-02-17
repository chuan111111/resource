import argparse
import base64
import os
import random
import socket
import threading
import mimetypes
import json
import datetime


# 创建ArgumentParser对象
parser = argparse.ArgumentParser(description='Server 参数示例')

# 从命令行添加参数
parser.add_argument('-i', '--ip', help='服务器IP地址')
parser.add_argument('-p', '--port', help='服务器端口号')

# 解析命令行参数
args = parser.parse_args()

# 获取参数值，之后直接用ip和port变量即可
ip = args.ip
port = int(args.port)

# 打印参数值
print(f'服务器IP地址：{ip}')
print(f'服务器端口号：{port}')

def log(message):
    current_time = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    log_entry = f"{current_time} - {message}\n"

    with open('server.log', 'a') as log_file:
        log_file.write(log_entry)

# 处理http-sustech参数，存进params字典
def parse_query_string(query_string):
    params = {}
    if '?' in query_string:
        query_string = query_string.split('?')[1]
        pairs = query_string.split('&')
        for pair in pairs:
            key, value = pair.split('=')
            params[key] = value
    return params


# 设置http回复的header，返回回复的content部分（为字节对象可以直接发送）
def set_response(content, content_type, response_header):
    response_header['Content-Type'] = content_type
    if isinstance(content, str):
        content = content.encode('utf-8')
    response_header['Content-Length'] = len(content)
    # test for forcing to download
    if content_type != 'text/html':
        response_header['Content-Disposition'] = 'attachment'
    return content  # 字节对象


# 相比set_response只是多一个返回error的html的content部分
def set_error(status_code, message, response_header):
    error_message = f'<html><body><h1>{status_code} {message}</h1></body></html>'
    return set_response(error_message.encode('utf-8'), 'text/html', response_header)


# 在html中添加文件列表，并且是链接的形式
def generate_list_items(items, current_directory):
    list_items = ''

    # 添加返回到跟目录的链接
    root_link = f'<li class="file"><a href="/">/</a></li>'
    list_items += root_link

    # 添加返回上一级菜单的链接
    parent_link = f'<li class="file"><a href="../">../</a></li>'
    list_items += parent_link

    for item in items:
        item_path = os.path.join(current_directory, item)
        if os.path.isdir(item_path):
            # 对于文件夹，添加链接
            list_items += f'<li class="file"><a href="{item}/">{item}/</a></li>'
        else:
            base_path = "data"  # 改为自己base_path的地址
            # 使用 replace 去掉指定部分
            item_path = item_path.replace(base_path, "")
            # 对于文件，同样添加链接
            list_items += f'<li class="file"><a href="{item_path}">{item}</a></li>'

    return list_items


def generate_directory_html(directory_path):
    items = os.listdir(directory_path)
    parent_directory = os.path.dirname(directory_path)

    # 生成文件列表
    list_items = generate_list_items(items, directory_path)

    # 构建整个 HTML 内容
    html_content = f'''
        <!DOCTYPE html>
        <html>
        <head>
            <title>Directory Listing</title>
            <style>
                body {{
                    font-family: Arial, sans-serif;
                }}
                .file-tree {{
                    list-style-type: none;
                    padding: 0;
                }}
                .file {{
                    padding-left: 20px;
                }}
            </style>
        </head>
        <body>
            <h1>Directory for {directory_path}</h1>
            <ul class="file-tree">

                {list_items}
            </ul>
        </body>
        </html>
        '''
    return html_content


# TODO: 需要改为返回一个python list而不是json
def generate_directory_json(directory_path):
    items = os.listdir(directory_path)
    return json.dumps(items)


def read_file_content(file_path):
    with open(file_path, 'rb') as f:
        return f.read()

def checkup(path, request_header, client_socket, response_header, response_first_line, method, username, file_content):
    print(path)
    if method != 'POST':
        response_first_line[0] = 'HTTP/1.1 405 Method Not Allowed\r\n'
        log(f"ERROR. [Upload] 405 Method Not Allowed.")
        return set_error(405, 'Method Not Allowed', response_header)
    else:
        # 根本没有path=
        if 'path=' not in path:
            response_first_line[0] = 'HTTP/1.1 400 Bad Request\r\n'
            log(f"ERROR. [Upload] 400 Bad Request.")
            print('no path= parameter')
            return set_error(400, 'Bad Request', response_header)
        # 有path=
        else:
            upload_user_dir = path.split('path=')[1].strip('/')
            upload_user = upload_user_dir.split('/')[0]
            print('dir: '+upload_user_dir)
            print('name: '+username)
            # =后面是空的
            if upload_user_dir == '':
                response_first_line[0] = 'HTTP/1.1 400 Bad Request\r\n'
                log(f"ERROR. [Upload] 400 Bad Request.")
                return set_error(400, 'Bad Request', response_header)
                print('cmd: 400 Bad Request')
            # =后面不空
            else:
                # 判断是否403/404
                if not os.path.exists('data/' + upload_user_dir):
                    response_first_line[0] = 'HTTP/1.1 404 Not Found\r\n'
                    log(f"ERROR. [Upload] 404 Not Found.")
                    return set_error(404, 'Not Found', response_header)
                    print('dir not exists!')
                elif username != upload_user or username == 'admin':
                    print('the dir exists!')
                    response_first_line[0] = 'HTTP/1.1 403 Forbidden\r\n'
                    log(f"ERROR. [Upload] 403 Forbidden.")
                    return set_error(403, 'Forbidden', response_header)
                else:
                    print('upload is legal!')
                    response_first_line[0] = 'HTTP/1.1 200 OK\r\n'
                    log(f"INFO. [Upload] User {username} successfully upload {path}.")
                    return client_post(client_socket, response_header, response_first_line, upload_user, upload_user_dir, request_header, file_content)



def checkdelete(path, request_header, client_socket, response_header, response_first_line, method,username):
    if method != 'POST':
        response_first_line[0] = 'HTTP/1.1 405 Method Not Allowed\r\n'
        log(f"ERROR. [Delete] 405 Method Not Allowed.")
        return set_error(405,'Method Not Allowed',response_header)
    else:

        return client_delete(path, request_header, client_socket, response_header, response_first_line,username)



def checkdown(path, request_header, client_socket, response_header, response_first_line, method):
    if method != 'GET':
        response_first_line[0] = 'HTTP/1.1 405 Method Not Allowed\r\n'
        log(f"ERROR. [Download] 405 Method Not Allowed.")
        return set_error(405,'Method Not Allowed',response_header)
    else:
        if 'Range' in request_header:
            return client_get_partial_content(path, request_header, client_socket, response_header, response_first_line)
        else:
            return client_get(path, request_header, client_socket, response_header, response_first_line)


def generate_body_content(file_path, ranges, type):
    boundary = "THISISMYSELFDIFINEDBOUNDARY"

    body = b''
    with open(file_path, 'rb') as f:
        for start, end in ranges:
            f.seek(start)
            content_part = f.read(end - start + 1)
            content_range = f"{start}-{end}/{os.path.getsize(file_path)}"
            content = f"--{boundary}\r\nContent-type: {type}\r\nContent-range: {content_range}\r\n\r\n{content_part.decode('utf-8')}\r\n"
            body += content.encode('utf-8')

    # 添加结束边界
    body += f"--{boundary}--\r\n".encode('utf-8')
    return body


# 处理range
def client_get_partial_content(path, request_header, client_socket, response_header, response_first_line):
    query_params = parse_query_string(path) if path != '/' else {}
    file_path = "data" + path

    range_request = request_header.get('Range')

    if os.path.exists(file_path):
        total_size = os.path.getsize(file_path)

        # 获取请求的范围
        ranges = range_request.split(',')
        content = b''

        ranges_list = []
        temp = None
        for r in ranges:
            try:
                if len(r.split('-')) != 2:
                    response_first_line[0] = 'HTTP/1.1 416 Range NotSatisfiable\r\n'
                    return set_error(416, "Range NotSatisfiable", response_header)
                start, end = map(int, r.split('-'))
            except ValueError as e:
                response_first_line[0] = 'HTTP/1.1 416 Range NotSatisfiable\r\n'
                return set_error(416, "Range NotSatisfiable", response_header)
            if end > total_size or start > end:
                response_first_line[0] = 'HTTP/1.1 416 Range NotSatisfiable\r\n'
                return set_error(416, "Range NotSatisfiable", response_header)
            end = min(end, total_size - 1)
            ranges_list.append((start, end))

            # 读取文件的指定部分
            with open(file_path, 'rb') as f:
                f.seek(start)
                read_content = f.read(end - start + 1)
                content += read_content + b'\r\n'
                temp = read_content
        # 获取请求的范围
        # start, end = 0, total_size - 1
        # if range_request:
        #     ranges = range_request.split('=')[1].split('-')
        #     start = int(ranges[0]) if ranges[0] else start
        #     end = int(ranges[1]) if ranges[1] else end
        #
        # # 读取文件的指定部分
        # with open(file_path, 'rb') as f:
        #     f.seek(start)
        #     content = f.read(end - start + 1)

        # 构建Content-Range和Content-Length响应头
        # response_header['Content-Range'] = f'bytes {start}-{end}/{total_size}'
        #response_header['Content-Length'] = len(content)
        response_first_line[0] = 'HTTP/1.1 206 Partial Content\r\n'

        body = generate_body_content(file_path, ranges_list, mimetypes.guess_type(file_path)[0])
        if len(ranges) == 1:
            response_header['Content-Range'] = str(start) + '-' + str(end) + '/' + str(os.path.getsize(file_path))
            return set_response(temp, mimetypes.guess_type(file_path)[0], response_header)
        elif len(ranges) > 1:
            return set_response(body, 'multipart/byteranges; boundary=THISISMYSELFDIFINEDBOUNDARY', response_header)
    else:
        response_first_line[0] = 'HTTP/1.1 404 Not Found\r\n'
        return set_error(404, "can not find", response_header)


def client_get(path, request_header, client_socket, response_header, response_first_line):
    query_params = parse_query_string(path) if path != '/' else {}
    sustech_http = int(query_params.get('SUSTech-HTTP', '0'))
    chunked = int(query_params.get('chunked', '0'))

    file_path = ("data" + path)
    print(file_path)
    print("sustech_http: ", sustech_http)
    print("chunked: ", chunked)
    log(f"INFO. [Download] sustech_http: {sustech_http}, chunked: {chunked}.")
    if "SUSTech-HTTP=" in file_path:
        file_path = file_path.split("?")[0][:-1]
    if "chunked=1" in file_path:
        file_path = file_path.split("?")[0]
    print(file_path)
    if os.path.exists(file_path):
        if os.path.isdir(file_path):
            if sustech_http == 0:
                print(file_path)
                response = generate_directory_html(file_path)
                response_first_line[0] = 'HTTP/1.1 200\r\n'
                log(f"INFO. [Download] HTTP=0.")
                return set_response(response, 'text/html', response_header)
            elif sustech_http == 1:
                response = generate_directory_json(file_path)
                response_first_line[0] = 'HTTP/1.1 200\r\n'
                log(f"INFO. [Download] HTTP=1.")
                json_response = json.dumps(response)  # 将列表转换为 JSON 字符串
                return set_response(json_response, "text/html", response_header)
            else:
                response_first_line[0] = 'HTTP/1.1 400 Bad Request\r\n'
                log(f"ERROR. [Download] 400 Bad Request.")
                return set_error(400, 'Invalid SUSTech-HTTP parameter', response_header)
        elif os.path.isfile(file_path):
            if chunked:
                # 使用chunked transfer encoding传输文件内容
                with open(file_path, 'rb') as file:
                    chunk_size = 3  # 设置每个chunk的大小

                    response_first_line[0] = 'HTTP/1.1 200 OK\r\n'
                    response_header['Transfer-Encoding'] = 'chunked'
                    data = file.read(chunk_size)
                    response = b''


                    while data:
                        # 发送chunk length和chunk data
                        length_bytes = bytes(str(len(data)), 'utf-8')
                        response += length_bytes + b'\r\n' + data + b'\r\n'
                        data = file.read(chunk_size)

                    # 发送chunked transfer的结束标志
                    response += b'0\r\n\r\n'
                    log(f"INFO. [Download] Successfully downloaded {file_path} using chunked method.")
                    print(response)
                    return set_response(response, 'text/html', response_header)
            else:
                # 如果没有指定chunked，使用原始的方法发送整个文件内容
                response = read_file_content(file_path)
                response_first_line[0] = 'HTTP/1.1 200 OK\r\n'
                log(f"INFO. [Download] Successfully downloaded {file_path}.")
                return set_response(response, mimetypes.guess_type(file_path)[0] or 'application/octet-stream',response_header)
    else:
        print("error")
        response_first_line[0] = 'HTTP/1.1 404 Not Found\r\n'
        log(f"ERROR. [Download] 404 Not Found.")
        return set_error(404, "can not find", response_header)


def client_delete(path, request_header, client_socket, response_header, response_first_line,username):
    print("Into the delete")
    if 'path=' not in path:
        response_first_line[0] = 'HTTP/1.1 400 Bad Request\r\n'
        log(f"ERROR. [Delete] 400 Bad Request.")
        return set_error(400, 'Bad Request', response_header)
    query_params = parse_query_path(path) if path != '/' else {}
    print("query_params: ", query_params)
    delete_path = query_params.get('path')
    delete_path_parts = delete_path.split('/')
    # 构建目录路径

    print("delete_path: ",delete_path)
    user_directory = os.path.join(os.getcwd(), 'data', delete_path_parts[0])
    user_directory_file = os.path.join(os.getcwd(), 'data', delete_path)


    if not os.path.exists(user_directory_file):
        response_first_line[0] = 'HTTP/1.1 404 Not Found\r\n'
        log(f"ERROR. [Delete] 404 Not Found.")

        return set_error(404, 'Not Found: Target directory or file does not exist', response_header)


    if not username == delete_path_parts[0] or username == 'admin':
        response_first_line[0] = 'HTTP/1.1 403 Forbidden\r\n'
        log(f"ERROR. [Delete] 403 Forbidden.")
        return set_error(403, 'Forbidden', response_header)

    try:
        # 删除文件
        os.remove(user_directory_file)
        # 或者删除目录及其内容
        # shutil.rmtree(user_directory)
    except Exception as e:
        print(f"Error deleting file: {e}")
        log(f"ERROR. [Delete] 500 {e}.")
        return set_error(500, 'Internal Server Error: Failed to delete file', response_header)

    # 发送成功的响应
    log(f"INFO. [Delete] Successfully deleted {path}.")
    return set_response('HTTP/1.1 200 OK\r\n', 'text/html', response_header)


def parse_query_path(query_string):
    params = {}
    if '?' in query_string:
        query_string = query_string.split('?')[1]
        pairs = query_string.split('&')
        for pair in pairs:
            key, value = pair.split('=')
            params[key] = value
    return params


def client_delete(path, request_header, client_socket, response_header, response_first_line,username):
    print("Into the delete")
    if 'path=' not in path:
        response_first_line[0] = 'HTTP/1.1 400 Bad Request\r\n'
        return set_error(400, 'Bad Request', response_header)
    query_params = parse_query_path(path) if path != '/' else {}
    print("query_params: ", query_params)
    delete_path = query_params.get('path')
    delete_path_parts = delete_path.split('/')


    # 构建目录路径

    print("delete_path: ",delete_path)
    user_directory = os.path.join(os.getcwd(), 'data', delete_path_parts[0])
    user_directory_file = os.path.join(os.getcwd(), 'data', delete_path)


    if not os.path.exists(user_directory_file):
        response_first_line[0] = 'HTTP/1.1 404 Not Found\r\n'
        return set_error(404, 'Not Found: Target directory or file does not exist', response_header)


    if not username == delete_path_parts[0]:
        response_first_line[0] = 'HTTP/1.1 403 Forbidden\r\n'
        return set_error(403, 'Forbidden', response_header)

    try:
        # 删除文件
        os.remove(user_directory_file)
        # 或者删除目录及其内容
        # shutil.rmtree(user_directory)
    except Exception as e:
        print(f"Error deleting file: {e}")
        return set_error(500, 'Internal Server Error: Failed to delete file', response_header)

    # 发送成功的响应
    return set_response('HTTP/1.1 200 OK\r\n', 'text/html', response_header)

def client_head():
    return


def extract_filename_and_content(part):
    filename_start = part.find(b'filename="') + len(b'filename="')
    filename_end = part.find(b'"', filename_start)
    filename = part[filename_start:filename_end].decode('utf-8') if filename_start != -1 else 'uploaded_file'

    content_start = part.find(b'\r\n\r\n') + len(b'\r\n\r\n')
    content_end = part.find(b'\r\n--', content_start)
    content = part[content_start:content_end] if content_start != -1 else b''

    return filename, content


def client_post(client_socket, response_header, response_first_line, upload_user, upload_user_dir, request_header, file_content):
    boundary = request_header.get('Content-Type', '').split('boundary=')[1]
    parts = file_content.split(b'--' + boundary.encode())

    # Write the content to the target file
    for part in parts:
        if b'filename=' in part and b'Content-Disposition' in part:
            filename, file_content = extract_filename_and_content(part)

            # Write the content to the target file

            file_path = os.path.join('data', upload_user_dir, filename)
            print(file_path)
            with open(file_path, 'wb') as f:
                f.write(file_content)
    return set_response('', 'text/html', response_header)


user_password = {'client1': '123', 'client2': '123', 'client3': '123','admin':'123'}
cookies = {}
cookies_time = {}
exceed_time = 10

def assem(first_line, header, content):
    for key, value in header.items():
        first_line[0] += f"{key}: {value}\r\n"
    first_line[0] += '\r\n'
    result = first_line[0].encode('utf-8') + content
    print(first_line[0])
    return result


def separate_header_and_content(client_socket):
    client_socket.settimeout(0.5)  # 设置超时时间为 0.5 秒
    # 初始化空的 header 和 content 变量
    header = b""
    content = b""
    # 循环读取数据，直到头部结束
    while True:
        try:
            data = client_socket.recv(8096)
            if data == b"":
                break

            header += data  # 将接收到的数据追加到 header 中
        except socket.timeout:
            break  # 超时退出循环

    if b"\r\n\r\n" in header:
        # 找到头部结束的标志
        header, content = header.split(b"\r\n\r\n", 1)

    return header, content


def handle_request(client_socket, client_address):
    while True:
        request, file_content = separate_header_and_content(client_socket)
        request = request.decode('utf-8')
        # print(f"处理来自 {client_address} 的连接")
        if request:
            print('REQUEST')
            # 根据header判断是否persist连接
            persistent = True
            # 解析方法路径以及各个header字段并且放进字典
            request_header = {}
            response_header = {}
            response_first_line = ['']
            raw_list = request.split("\r\n")
            if len(raw_list) < 1:
                # 构建400 bad request的response，也就是下面这个还要完善
                response_first_line[0] = 'HTTP/1.1 400 Bad Request\r\n'
            else:
                method, path, version = raw_list[0].split()
                print(method)
                print(path)
                for index in range(1, len(raw_list)):
                    item = raw_list[index].split(":", 1)
                    if len(item) == 2:
                        request_header.update({item[0].lstrip(' '): item[1].lstrip(' ')})
                print(request_header)
            log(f"INFO. Handling request from {client_address}, Method: {method}, Request_Header: {request_header}")

            if request_header.get('Connection') == 'Close':
                # response_header.update({'Connection': 'Close'})
                log(f"INFO. [Connection] Persistent Connect: Close.")
                persistent = False
            else:
                log(f"INFO. [Connection] Persistent Connect: keep-alive.")
                response_header.update({'Connection': 'keep-alive'})

            # for icon
            # if method == 'get' and path == '/favicon.ico':
            #     con_to_send = client_get(path, request_header, client_socket, response_header, response_first_line)
            #     response = assem(response_first_line, response_header, con_to_send)
            #     client_socket.send(response)
            #     continue

            # 用户认证
            username = None  # 新增
            if 'Authorization' not in request_header and 'Cookie' not in request_header:
                response_first_line[0] = 'HTTP/1.1 401 Unauthorized\r\n'
                response_header.update({'WWW-Authenticate': 'Basic realm="Authorization Required"'})
                log(f"INFO. [WWW-Authenticate] Authorization Required.")
                # test for authorization
                con_to_send = set_response(''.encode('utf-8'), 'text/html', response_header)
                response = assem(response_first_line, response_header, con_to_send)
                client_socket.send(response)
                continue
            else:
                session_id_out_of_date = False
                if 'Cookie' in request_header:
                    id = request_header['Cookie'].split('=')[1]
                    if id in cookies:
                        time_interval = (datetime.datetime.now() - cookies_time[id]).total_seconds()
                        print(time_interval)
                        if time_interval <= exceed_time:
                            response_first_line[0] = 'HTTP/1.1 200 OK\r\n'
                            username = cookies[id]
                            cookies_time[id] = datetime.datetime.now()
                        else:
                            session_id_out_of_date = True
                            del cookies[id]
                            del cookies_time[id]
                    else:
                        session_id_out_of_date = True
                if 'Cookie' not in request_header or session_id_out_of_date:
                    if 'Authorization' not in request_header:
                        response_first_line[0] = 'HTTP/1.1 401 Unauthorized\r\n'
                        response_header.update({'WWW-Authenticate': 'Basic realm="Authorization Required"'})
                        log(f"INFO. [WWW-Authenticate] Authorization Required.")
                        # test for authorization
                        con_to_send = set_response(''.encode('utf-8'), 'text/html', response_header)
                        response = assem(response_first_line, response_header, con_to_send)
                        client_socket.send(response)
                        continue
                    else:
                        print('validation')
                        user_password_info = request_header.get('Authorization').lstrip(' ')
                        print(user_password_info)
                        content = user_password_info.split(' ')[1]
                        decode = base64.b64decode(content).decode('utf-8')
                        username = decode.split(':')[0]
                        password = decode.split(':')[1]
                        print(username)
                        print(password)
                        if username in user_password:
                            if password == user_password.get(username):
                                response_first_line[0] = 'HTTP/1.1 200 OK\r\n'
                                id = ''.join(str(random.randint(0, 9)) for i in range(8))
                                # 第一次产生session id
                                cookies[id] = username
                                cookies_time[id] = datetime.datetime.now()
                                response_header['Set-Cookie'] = 'session-id=' + id
                                print(cookies)
                                log(f"INFO. Successfully Validated, username: {username}, password: {password}, cookies: {cookies}.")
                            else:
                                response_first_line[0] = 'HTTP/1.1 401 Unauthorized\r\n'
                                con_to_send = set_response(''.encode('utf-8'), 'text/html', response_header)
                                response = assem(response_first_line, response_header, con_to_send)
                                client_socket.send(response)
                                log(f"ERROR. Unauthorized: wrong password, username: {username}.")
                                continue
                        else:
                            response_first_line[0] = 'HTTP/1.1 401 Unauthorized\r\n'
                            con_to_send = set_response(''.encode('utf-8'), 'text/html', response_header)
                            response = assem(response_first_line, response_header, con_to_send)
                            client_socket.send(response)
                            log(f"ERROR. Unauthorized: no such user, username: {username}.")
                            continue

            if path.strip('/').split('/')[0] == 'secretWeb':
                print('into')
                response_first_line[0] = 'HTTP/1.1 301 Moved Permanently\r\n'
                response_header['Location'] = 'https://www.sustech.edu.cn/'
                con_to_send = set_response(''.encode('utf-8'), 'text/html', response_header)
                response = assem(response_first_line, response_header, con_to_send)
                client_socket.send(response)
                if not persistent:
                    client_socket.close()
                    print(f"来自 {client_address} 的连接已关闭")
                    break
                continue

            if path.strip('/').split('/')[0] == 'main':
                print('into')
                response_first_line[0] = 'HTTP/1.1 301 Moved Permanently\r\n'
                response_header['Location'] = 'http://'+ip+':8080'
                con_to_send = set_response(''.encode('utf-8'), 'text/html', response_header)
                response = assem(response_first_line, response_header, con_to_send)
                client_socket.send(response)
                if not persistent:
                    client_socket.close()
                    print(f"来自 {client_address} 的连接已关闭")
                    break
                continue

            #match = re.search(r'\/([^?]+)', path)
            match = path.split('?')[0].split('/')[-1]

            if match:

                if match == 'upload':
                    log(f"INFO. User {username} request upload.")
                    con_to_send = checkup(path, request_header, client_socket, response_header, response_first_line, method, username, file_content)

                elif match == 'delete':
                    log(f"INFO. User {username} request delete.")
                    con_to_send = checkdelete(path, request_header, client_socket, response_header, response_first_line,
                                              method, username)

                else:
                    log(f"INFO. User {username} request download.")
                    con_to_send = checkdown(path, request_header, client_socket, response_header, response_first_line,
                                            method)

            else:
                if method == 'GET':
                    log(f"INFO. User {username} request download.")
                    con_to_send = checkdown(path, request_header, client_socket, response_header, response_first_line,
                                            method)
                else:
                    log(f"INFO. User {username} request check login.")
                    if method == 'HEAD':
                        response_first_line[0] = 'HTTP/1.1 200 OK\r\n'
                        log(f"INFO. User {username} successfully checked login.")
                        con_to_send = set_response('', 'text/html', response_header)
                    else:
                        response_first_line[0] = 'HTTP/1.1 405 Method Not Allowed\r\n'
                        log(f"ERROR. 405 Method Not Allowed.")
                        con_to_send = set_error(405, 'Method Not Allowed', response_header)

            # if request_header.get('Connection') == 'Close':
            #     response_header.update({'Connection': 'Close'})
            #     persistent = False
            # else:
            #     response_header.update({'Connection': 'keep-alive'})
            # if method == 'GET':
            #     con_to_send = client_get(path, request_header, client_socket, response_header, response_first_line)
            # elif method == 'HEAD':
            #     client_head()
            # elif method == 'POST':
            #     client_post()
            # elif method == 'get' and path == '/favicon.ico':
            #     con_to_send = client_get(path, request_header, client_socket, response_header, response_first_line)

            # 组装和发送http response
            response = assem(response_first_line, response_header, con_to_send)
            client_socket.send(response)
            if not persistent:
                client_socket.close()
                print(f"来自 {client_address} 的连接已关闭")
                log(f"INFO. Closed connection from {client_address}")
                break


def start_server(host, port):
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server_socket.bind((host, port))
    server_socket.listen(10)
    print(f"Server listening on {host}:{port}...")
    log(f"INFO. Server listening on {ip}:{port}...")

    while True:
        client_socket, client_address = server_socket.accept()
        log(f"INFO. Accepting connection from {client_address}")
        # print('accept!!!')
        client_thread = threading.Thread(target=handle_request, args=(client_socket, client_address))
        client_thread.start()


if __name__ == "__main__":
    start_server(ip, port)

