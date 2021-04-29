# netmate

play around with 
System.exit(0) - safely closed socket
connection rest by peer - System.halt() - force close server
connection reset - force close client
connection refused - start cleint when server is not started
Software caused connection abort: recv failed - close socket, not shutdown server's jvm, client send a request
