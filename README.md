This is a cook-book of java.net UDP and TCP communication using sockets.
there are several ways of communication listed:


> I. Balancer.java is a UDP based pair of BroadcastSender and BroadcastReceiver that are using DatagramSocket and DatagramPacket on Broadcast Internet Address (255.255.255.255) to send a message to come of existed receivers.


> II. Broadcast.java is a UDP based pair of MultycastSender and MultycastReceiver that are using the MulticastSocket NetworkInterface in front of DatagramSocket and DatagramPacket to make a real broadcast, one sender to each receiver.


> III. TCPChat.java is a TCP based pair of TCPServer and TCPClient that are using ServerSocket and Socket to achieve two-way communication.


> IV. UDPUnicast.java simple UDP based pair of UDPSender and UReceiver that are using DatagramSocket and DatagramPacket to send uni-cast message.

