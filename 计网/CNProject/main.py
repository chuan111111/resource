import socket
import json
import hashlib

def start_server():
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind(('localhost', 8081))
    server_socket.listen(1)

    print("等待客户端连接...")

    client_socket, addr = server_socket.accept()

    # 生成Diffie-Hellman密钥
    private_key = 123  # 这个数字是私密的，实际应用中应该更随机和安全
    base = 5  # 这个数字是公开的，称为基数
    modulus = 23  # 这个数字也是公开的，称为模数

    # 发送基数和模数给客户端
    public_data = {
        'base': base,
        'modulus': modulus
    }
    client_socket.send(json.dumps(public_data).encode())

    # 接收客户端发送的公开数据
    client_public_data = json.loads(client_socket.recv(1024).decode())
    client_base = client_public_data['base']
    client_modulus = client_public_data['modulus']

    # 计算共享密钥
    shared_key = (client_base ** private_key) % client_modulus

    print("共享密钥:", shared_key)

    # 使用哈希函数派生对称密钥
    symmetric_key = hashlib.sha256(str(shared_key).encode()).digest()

    print("派生的对称密钥:", symmetric_key)

    client_socket.close()
    server_socket.close()

if __name__ == "__main__":
    start_server()
