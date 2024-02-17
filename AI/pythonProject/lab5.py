import dns.message
import dns.query
import dns.resolver
from dns.rdatatype import A, AAAA, CNAME, NS, MX

def dns_server(query_name, query_type, rd_flag):
    if rd_flag == 0:
        recursive = False
    else:
        recursive = True

    request = dns.message.make_query(query_name, query_type, use_edns=False)
    request.flags.rd = recursive

    response = dns.query.tcp(request, '8.8.8.8')

    for answer in response.answer:
        if query_type == A or query_type == AAAA:
            print(f"Answer: {answer.name} has address {answer[0].address}")
        elif query_type == CNAME:
            print(f"Answer: {answer.name} is an alias for {answer[0].target}")
        elif query_type == NS:
            print(f"Answer: {answer.name} name server = {answer[0].target}")
        elif query_type == MX:
            print(f"Answer: {answer.name} mail exchanger = {answer[0].exchange}")

    if response.flags.AA:
        print("The answer is from an authoritative Name Server.")
    else:
        print("The answer is not from an authoritative Name Server.")

if __name__ == "__main__":
    query_name = "example.com"  # Replace with your desired domain name
    query_type = A  # Replace with the desired query type (A, AAAA, CNAME, NS, MX)
    rd_flag = 1  # Set RD flag to 0 or 1 for recursive or non-recursive queries

    dns_server(query_name, query_type, rd_flag)
