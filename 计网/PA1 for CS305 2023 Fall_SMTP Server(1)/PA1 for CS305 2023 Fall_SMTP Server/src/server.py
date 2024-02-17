from __future__ import annotations

from argparse import ArgumentParser
from email import message_from_string
from queue import Queue
import socket
from socketserver import ThreadingTCPServer, BaseRequestHandler
from threading import Thread

import tomli


def student_id() -> int:
    return 12110848  # TODO: replace with your SID


parser = ArgumentParser()
parser.add_argument('--name', '-n', type=str, required=True)
parser.add_argument('--smtp', '-s', type=int)
parser.add_argument('--pop', '-p', type=int)

args = parser.parse_args()

with open('data/config.toml', 'rb') as f:
    _config = tomli.load(f)
    SMTP_PORT = args.smtp or int(_config['server'][args.name]['smtp'])
    POP_PORT = args.pop or int(_config['server'][args.name]['pop'])
    ACCOUNTS = _config['accounts'][args.name]
    MAILBOXES = {account: [] for account in ACCOUNTS.keys()}

with open('data/fdns.toml', 'rb') as f:
    FDNS = tomli.load(f)

ThreadingTCPServer.allow_reuse_address = True


def fdns_query(domain: str, type_: str) -> str | None:
    domain = domain.rstrip('.') + '.'
    return FDNS[type_][domain]





class POP3Server(BaseRequestHandler):
    def handle(self):
        conn = self.request

        conn.send(f'+OK {student_id()}\'s dummy POP3 server ready\r\n'.encode())
        account = ''
        dele_mail = set()
        login = False
        while True:
            msg = conn.recv(1024).strip().decode()
            cmd = msg.upper()
            if cmd.startswith('USER'):
                user = msg.split()[1]
                if ACCOUNTS.__contains__(user):
                    conn.sendall(b'+OK user found\r\n')
                    account = user
                else:
                    conn.sendall(b'-ERR invalid user\r\n')
            elif cmd.startswith('PASS'):
                password = msg.split()[1]
                if account == '':
                    conn.sendall(b'-ERR please specific user\r\n')
                elif ACCOUNTS[account] != password:
                    conn.sendall(b'-ERR wrong password\r\n')
                else:
                    login = True
                    conn.sendall(b'+OK user successfully logged on\r\n')
            elif cmd.startswith('STAT'):
                if login:
                    mail_total = 0
                    byts_total = 0
                    for i in range(len(MAILBOXES[account])):
                        if i not in dele_mail:
                            mail_total += 1
                            byts_total += len(MAILBOXES[account][i])
                    conn.sendall(f'+OK {mail_total} {byts_total}\r\n'.encode())
                else:
                    conn.sendall(b'-ERR log in first\r\n')
            elif cmd.startswith('LIST'):
                if login:
                    conn.sendall(f'+OK {len(MAILBOXES[account])} messages\r\n'.encode())
                    for i in range(len(MAILBOXES[account])):
                        if i not in dele_mail:
                            mail = MAILBOXES[account][i]
                            size = len(mail)
                            conn.sendall(f'{i + 1} {size}\r\n'.encode())
                    conn.sendall(b'.\r\n')
                else:
                    conn.sendall(b'-ERR log in first\r\n')
            elif cmd.startswith('RETR'):
                if login:
                    id = int(msg.split()[1]) - 1
                    if id >= len(MAILBOXES[account]) or id < 0 or id in dele_mail:
                        conn.sendall(b'-ERR wrong mail id\r\n')
                        continue
                    mail_message = MAILBOXES[account][id]
                    mail = mail_message.strip()
                    conn.sendall('+OK\r\n'.encode())
                    conn.sendall((mail + '\r\n').encode())
                    # conn.sendall(b'.\r\n')
                else:
                    conn.sendall(b'-ERR log in first\r\n')
            elif cmd.startswith('DELE'):
                if login:
                    id = int(msg.split()[1]) - 1
                    print("id=" + str(id))
                    if id >= len(MAILBOXES[account]) or id < 0:
                        conn.sendall(b'-ERR wrong mail id\r\n')
                        continue
                    dele_mail.add(id)
                    conn.sendall(f'+OK \r\n'.encode())
                else:
                    conn.sendall(b'-ERR log in first\r\n')
            elif cmd.startswith('RSET'):
                if login:
                    dele_mail.clear()
                    conn.sendall(b'+OK\r\n')
                else:
                    conn.sendall(b'-ERR log in first\r\n')
            elif cmd.startswith('NOOP'):
                if login:
                    conn.sendall(b'+OK\r\n')
                else:
                    conn.sendall(b'-ERR log in first\r\n')
            elif cmd.startswith('QUIT'):
                for i in dele_mail:
                    MAILBOXES[account].remove(i)
                dele_mail.clear()
                conn.sendall(f'+OK {student_id()}\'s dummy POP3 server signing off\r\n'.encode())
                conn.close()
                break
            else:
                conn.sendall(b'-ERR illegal command\r\n')


class SMTPServer(BaseRequestHandler):
    def handle(self):
        conn = self.request
        conn.send(b"220 SMTP server ready\r\n")
        print(1)
        sender = None
        recipients = []
        while True:
            try:
                data = conn.recv(1024).decode('utf-8').strip()
                print("data = " + data)
                if not data:
                    break
                if data.upper().startswith("HELO ") or data.upper().startswith("EHLO "):
                    print(2)
                    conn.send(b"250 Hello\r\n")
                elif data.upper().startswith("MAIL FROM:"):
                    print(3)
                    sender = data.split(":")[1]
                    sender = sender[1:-1]
                    print("Sender = " + sender)
                    conn.send(b"250 OK\r\n")
                elif data.upper().startswith("RCPT TO:"):
                    print("RCPT ---- " + data)
                    recipient = data.split(":")[1]
                    recipient = recipient[1:-1]
                    recipients.append(recipient)
                    conn.send(b"250 Recipient OK\r\n")

                elif data.upper() == "DATA":
                    print(5)
                    if sender and recipients:
                        conn.send(b"354 Enter message, ending with \".\" on a line by itself\r\n")
                        print(7)
                        message_data = ""

                        while True:
                            line = conn.recv(1024).decode('utf-8')

                            if "\r\n.\r\n" in line:
                                print(line)
                                message_data += line
                                break
                            message_data += line
                        print("message =" + message_data)
                        for recipient in recipients:
                            print(recipient)
                            domain = recipient.split('@')[-1]
                            print("domain = " + domain)
                            num = _config['agent'][domain]['smtp'][5:]
                            print("num =" + num)
                            num = int(_config['server'][num]['smtp'])
                            print("num:" + str(num))
                            SMTP_SERVER1 = fdns_query(domain, 'MX')
                            print("smtp_server1 = " + SMTP_SERVER1)
                            domain1 = args.name
                            num2 = int(_config['server'][args.name]['smtp'])
                            print("num2=" + str(num2))
                            print("domain1 = " + domain1)
                            print("num=num2? " + str(num == num2))
                            if num == num2:
                                print("save message: " + message_data)
                                MAILBOXES[recipient].append(message_data)
                                print("save to local box: " + str(message_data))
                                conn.sendall(b'250 OK\r\n')

                            else:
                                domain = domain + "."
                                SMTP_SERVER = fdns_query(domain, 'MX')
                                conn1 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                                conn1.connect(('localhost', int(fdns_query(SMTP_SERVER, 'P'))))
                                print("localhost:" + fdns_query(SMTP_SERVER, 'P'))

                                res2 = conn1.recv(1024).strip().decode()
                                print("res2 = " + res2)
                                if res2.startswith('220'):
                                    conn1.sendall(b'EHLO Server1\r\n')
                                else:
                                    conn1.sendall(b'-ERR \r\n')
                                res2 = conn1.recv(1024).strip().decode()
                                print("res2 = " + res2)
                                if res2.startswith('250'):
                                    conn1.sendall(f'MAIL FROM:<{sender}>\r\n'.encode())
                                else:
                                    conn1.sendall(b'-ERR \r\n')
                                res2 = conn1.recv(1024).strip().decode()
                                print("res2 = " + res2)
                                if res2.startswith('250'):
                                    conn1.sendall(f'RCPT TO:<{recipient}>\r\n'.encode())
                                else:
                                    conn1.sendall(b'-ERR \r\n')

                                res2 = conn1.recv(1024).strip().decode()
                                print("res2 = !" + res2)
                                if res2.startswith('250'):
                                    conn1.sendall(b'data\r\n')
                                else:
                                    conn1.sendall(b'-ERR \r\n')
                                re = conn1.recv(1024).strip().decode()
                                print("re = " + re)

                                if re.startswith('354'):
                                    print("send message: " + message_data)
                                    conn1.sendall(f"{message_data}".encode('utf-8'))
                                else:
                                    conn1.sendall(b'-ERR \r\n')

                                res2 = conn1.recv(1024).strip().decode()
                                print("res2 = ?" + res2)
                                if res2.startswith('250'):
                                    conn1.sendall(b'quit\r\n')
                                else:
                                    conn1.sendall(b'-ERR \r\n')
                                res2 = conn1.recv(1024).strip().decode()
                                print("res2 = " + res2)
                                if res2.startswith('221'):
                                    conn1.close()
                                else:
                                    conn1.sendall(b'-ERR \r\n')

                        conn.send(b"250 Message accepted for delivery\r\n")
                    else:
                        conn.send(b"503 Bad sequence of commands\r\n")
                elif data.upper() == "QUIT":
                    print(6)
                    conn.send(b"221 Bye\r\n")
                    break
                else:
                    conn.send(b"500 Command not recognized\r\n")
            except Exception as e:
                conn.send(b"421 Service not available, closing connection\r\n")


if __name__ == '__main__':
    if student_id() % 10000 == 0:
        raise ValueError('Invalid student ID')

    smtp_server = ThreadingTCPServer(('', SMTP_PORT), SMTPServer)
    pop_server = ThreadingTCPServer(('', POP_PORT), POP3Server)
    Thread(target=smtp_server.serve_forever).start()
    Thread(target=pop_server.serve_forever).start()