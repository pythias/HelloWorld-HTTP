<?php
$socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
if (socket_bind($socket, "127.0.0.1", 8080) == false) {
    die("socket_bind failed.");
}

if (socket_listen($socket) == false) {
    die("socket_listen failed.");
}

while (true) {
    $client = socket_accept($socket);
    if ($client) {
        $request = socket_read($client, 2048);
        socket_write($client, "HTTP/1.1 200 OK\r\n");
        socket_write($client, "Connection: close\r\n");
        socket_write($client, "Content-Type: text/html; charset=UTF-8\r\n\r\n");
        socket_write($client, "Hello World!");
        socket_close($client);
    } else {
        usleep(10000);
    }
}