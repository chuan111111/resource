import socket
import json

def start_client():
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client_socket.connect(('localhost', 8081))

    # 接收服务器发送的公开数据
    server_public_data = json.loads(client_socket.recv(1024).decode())
    server_base = server_public_data['base']
    server_modulus = server_public_data['modulus']

    # 生成Diffie-Hellman密钥
    private_key = 456  # 这个数字是私密的，实际应用中应该更随机和安全

    # 发送客户端的公开数据
    public_data = {
        'base': server_base,
        'modulus': server_modulus
    }
    client_socket.send(json.dumps(public_data).encode())

    # 计算共享密钥
    shared_key = (server_base ** private_key) % server_modulus

    print("共享密钥:", shared_key)

    client_socket.close()

if __name__ == "__main__":
    start_client()
