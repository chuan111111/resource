import argparse
import base64
import os
import socket
import threading
import mimetypes
import json

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
def generate_list_items(items, directory_path):
    list_items = ''
    for item in items:
        item_path = os.path.join(directory_path, item)
        if os.path.isdir(item_path):
            list_items += f'<li class="file">{item}/</li>'
        else:
            base_path = "C:\\Users\\lyc\\PycharmProjects\\http\\data" # 改为自己base_path的地址
            # 使用 replace 去掉指定部分
            item_path = item_path.replace(base_path, "")
            list_items += f'<li class="file"><a href="{item_path}">{item}</a></li>'
    return list_items


def generate_directory_html(directory_path):
    items = os.listdir(directory_path)
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
                <li class="file">./</li>
                <li class="file">../</li>
                {generate_list_items(items, directory_path)}
            </ul>
        <!-- Upload Form -->
        <form id="uploadForm" enctype="multipart/form-data" method="post" onsubmit="uploadFile();">
        <input type="text" name="path" id="path" placeholder="Enter upload path">
        <input type="file" name="file" id="fileInput" accept=".txt, .jpg, .png">
        <button type="submit">Upload</button>
        </form>

        <script>
            function uploadFile() {{
                const fileInput = document.getElementById('fileInput');
                const file = fileInput.files[0];
                const uploadPath = document.getElementById('uploadPath').value;
                if (file && uploadPath) {{
                    const formData = new FormData();
                    formData.append('file', file);

                    fetch(`http://localhost:8080/upload?path=document.getElementById('uploadPath').value', {{
                        method: 'POST',
                        body: formData
                    }})
                    .then(response => {{
                        if (response.ok) {{
                            // Successful upload, you might want to update the file list
                            updateFileList();
                        }} else {{
                            console.error('Upload failed:', response.statusText);
                        }}
                    }})
                    .catch(error => {{
                        console.error('Error during upload:', error);
                    }});
                }} else {{
                    alert('Please select a file to upload.');
                }}
            }}
        </script>
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


def client_get(path, request_header, client_socket, response_header, response_first_line):
    query_params = parse_query_string(path) if path != '/' else {}
    sustech_http = int(query_params.get('SUSTech-HTTP', '0'))
    file_path = os.path.join(os.getcwd(), 'data', path.split('?')[0][1:].split('/')[0])
    print(path)
    print(file_path)
    if os.path.exists(file_path):
        if os.path.isdir(file_path):
            if sustech_http == 0:
                response = generate_directory_html(file_path)
                response_first_line[0] = 'HTTP/1.1 200\r\n'
                return set_response(response, 'text/html', response_header)
            elif sustech_http == 1:
                response = generate_directory_json(file_path)
                response_first_line[0] = 'HTTP/1.1 200\r\n'
                print('into arg=1')
                return set_response(response, 'application/json', response_header)
            else:
                response_first_line[0] = 'HTTP/1.1 400 Bad Request\r\n'
                return set_error(400, 'Invalid SUSTech-HTTP parameter', response_header)
        elif os.path.isfile(file_path):
            # If it's a GET request, read the file content and send it
            response = read_file_content(file_path)
            response_first_line[0] = 'HTTP/1.1 200 OK\r\n'
            return set_response(response, mimetypes.guess_type(file_path)[0] or 'application/octet-stream', response_header)
            # else:
            #     # If it's not a GET request, return an HTML page with a link to the file
            #     link_html = f'<html><body><a href="{file_path}" download>{file_path}</a></body></html>'
            #     self.send_response(link_html.encode('utf-8'), 200, 'text/html')
    else:
        print("error")
        response_first_line[0] = 'HTTP/1.1 Not Found\r\n'
        return set_error(404, "can not find", response_header)


def client_post(path, request_header, client_socket, response_header, response_first_line,username):
    print("into the post")
    query_params = parse_query_string(path) if path != '/' else {}
    print(query_params)
    upload_path = query_params.get('path')
    print(upload_path)
    if not upload_path:
       print('a')
       return set_error(400, 'Bad Request',response_header)
    if not username==upload_path[1:]:
       return set_error(403, 'Forbidden', response_header)

    user_directory = os.path.join(os.getcwd(), 'data', upload_path[1:])
    if not os.path.exists(user_directory):
       return set_error(404, 'Not Found: Target directory does not exist',response_header)

    # Read the content length from headers
    content_length_header = request_header.get('Content-Length')
    content_length = int(content_length_header) if content_length_header else 0

    content = b''
    # Read content from the connection in chunks
    while len(content) < content_length:
        chunk = client_socket.recv(1024)
        if not chunk:
            break
        content += chunk

    # Read the binary file content from the request


    # Write the content to the target file
    file_path = os.path.join(user_directory, 'uploaded_file')  # Change the filename as needed
    with open(file_path, 'wb') as f:
        f.write(content)

       # Send a successful response
    return  set_response('', 'text/html', response_header)


def client_head():
    return





user_password = {'student1': '123', 'student2': '234', 'student3': '345'}


def assem(first_line, header, content):
    for key, value in header.items():
        first_line[0] += f"{key}: {value}\r\n"
    first_line[0] += '\r\n'
    print(first_line[0])
    result = first_line[0].encode('utf-8') + content
    return result


def handle_request(client_socket, client_address):
    while True:
        request = client_socket.recv(1024).decode('utf-8')
        print(request)
        # print(f"处理来自 {client_address} 的连接")
        if request:
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
                print(1)
            # 用户认证
            # if 'Authorization' not in response_header:
            #     print(2)
            #     response_first_line[0] = 'HTTP/1.1 401 Unauthorized\r\n'
            #     response_header.update({'WWW-Authenticated': 'Basic realm"Authorization Required"'})
            else:
                print(3)
                # user_password_info = request_header.get('Authorization').lstrip(' ')
                # start_index = user_password_info.find('[')  # 查找左括号的索引位置
                # end_index = user_password_info.find(']')  # 查找右括号的索引位置
                # if start_index != -1 and end_index != -1:
                #     content = user_password_info[start_index + 1: end_index]
                #     decode = base64.b64decode(content).decode('utf-8')
                   # username = decode.split(':')[0]
                   #password = decode.split(':')[1]
                username="student1"
                password ="123"
                if username in user_password:
                    if password == user_password.get(username):
                        response_first_line[0] = 'HTTP/1.1 200 OK\r\n'
                    else:
                        response_first_line[0] = 'HTTP/1.1 401 Unauthorized\r\n'
                else:
                    response_first_line[0] = 'HTTP/1.1 401 Unauthorized\r\n'
            print(raw_list)
            method, path, version = raw_list[0].split()
            print(method)
            print(path)
            for index in range(1, len(raw_list)):
                item = raw_list[index].split(":", 1)
                if len(item) == 2:
                    request_header.update({item[0].lstrip(' '): item[1].lstrip(' ')})
            print(request_header)
            if request_header.get('Connection') == 'Close':
                response_header.update({'Connection': 'Close'})
                persistent = False
            else:
                response_header.update({'Connection': 'keep-alive'})

            if method == 'GET':
                con_to_send = client_get(path, request_header, client_socket, response_header, response_first_line)
            elif method == 'HEAD':
                client_head()
            elif method == 'POST':
                con_to_send=client_post(path, request_header, client_socket, response_header, response_first_line,username)

            # 组装和发送http response
            response = assem(response_first_line, response_header, con_to_send)
            client_socket.send(response)
            if not persistent:
                client_socket.close()
                print(f"来自 {client_address} 的连接已关闭")
                break


def start_server(host, port):
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server_socket.bind((host, port))
    server_socket.listen(10)
    print(f"Server listening on {host}:{port}...")

    while True:
        client_socket, client_address = server_socket.accept()
        print(client_socket)
        print(client_address)
        # print('accept!!!')
        client_thread = threading.Thread(target=handle_request, args=(client_socket, client_address))
        client_thread.start()


if __name__ == "__main__":
    start_server(ip, port)

