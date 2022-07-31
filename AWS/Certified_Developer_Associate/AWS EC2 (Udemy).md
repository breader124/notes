### Budget management
It's possible to analyze AWS bills in details to determine what service in which region incurred costs. It's also possible to see which services to which extent are free using AWS Free Tier.

Using billing console, it's possible to create budget with alerts notifying given email address about forecasted or actual budget shortage.

### EC2 basics
EC2 service has following capabilities:
- renting virtual machines (EC2)
- storing data on virtual drives (EBS)
- distributing load across machines (ELB)
- scaling the services using an auto-scaling group (ASG)

EC2 sizing and configuration options:
- operating system (Linux, Windows, MacOS)
- how much compute power & cores (CPU)
- how much random-access memory (RAM)
- how much storage space
- network card (its speed, public IP address)
- firewall rules (security groups)
- bootstrap script (EC2 User Data, executed with su privileges)

**bootstraping** - launching commands when a machine starts, it's run only once at the instance first start

### EC2 instance types
Naming convention for instance types `instance class.generation.size`

Instance types:
- general purpose (`t` class) - great of a diversity of workloads, balance between compute, memory and networking
- compute optimized (`c` class) - compute-intensive tasks that require high performance processord
- memory optimized (`r` class) - good performance when large data processing happens
- storage optimized(`i` class) - accessing lots of data on local storage, e.g. distributed file systems

Instance types can be compared using [ec2instances.info](https://instances.vantage.sh/).

### Security Groups basics
Security groups are the fundamental of network security in AWS. They control how traffic is allowed into or out of our EC2 instances. It's possible to think about them as a *firewall* for EC2 instances.

Security groups only contain **allow** rules and regulate:
- access to ports
- authorized IP ranges (both IPv4 and IPv6)
- control of inbound network
- control of outboound network

SG good to knows:
- can be attached to multiple instances
- locked down to a region/VPC combination
- does live outside the EC2 - if traffic is blocked, then EC2 instance won't see it
- it's good to maintain one separate SG for SSH access
- timout issues are probably caused by wrong SG configuration
- connection refused errors are most probably caused by application
- all inbound traffic is **blocked** by default
- all outbound traffic is **authorised** by default

SGs can reference either IP addresses or other security groups, it's useful for configuring load balancers.

Classic ports:
- 22 - SSH
- 21 - FTP (upload files into a file share)
- 22 - SFTP (upload files using SSH)
- 80 - HTTP (access unsecured websites)
- 443 - HTTPS (access secured websites)
- 3389 - RDP (remote desktop protocol for windows instances)