EC2 is like VM, but it's hosted in AWS instead of own data center. It's paid on demand (by default) and can be grown or shrinked in minutes.

#### EC2 pricing options
- on demand - pay by the hour or second, great for flexibility
- reserved - for 1 or 3 years, great if requirements are known, up to 72% discount
	- standard - tied to EC2 instance type
	- convertible - instance type can be changed to different one of the same or greater value
	- scheduled - launch within time window you define
- spot - purchase unused capacity, prices may fluctuate, great for very flexible applications, up to 90% discount
- dedicated - physical EC2 server dedicated for my use, great for satisfying specific compliance requirements or hardware-bound linceses

#### EC2 instance types (not required to remember them all)
Instance type determines the hardware of the host computer. Each instance type offeres different compute, memory and storage capabilities. They are grouped in instance families.

Instance type should be chosen based on application needs.

#### Understanding EBS volumes
SSD Volumes:
- gp2 - suitable for boot disks and general applications (3 IOPS per GiB, up to 16000 IOPS per volume)
- gp3 - suitable for boot disks and general applications, it's 20% cheaper than gp2 (baseline of 3000 IOPS for all volumes, up to 16000 IOPS per volume)
- io1 - suitable for OLTP and latency-sensitive apps (50 IOPS/GiB, up to 64000 IOPS per volume)
- io2 - suitable for OLTP and latency-sensitive apps (500 IOPS/GiB, up to 64000 IOPS per volume)
- io2 Block Express - for the largest, most critical, high-performance applications (up to 256000 IOPS per volume)

HDD Volumes:
- st1 - suitable for big data, data warehouses and ETL, cannot be boot volume
- sc1 - suitable for less-frequently-accessed data, cannot be boot volume

IOPS - metric showing how many read/writes per second can happen
Throughput - metric showing how many bits can be stored/retrieved per second

EBS volume created from encrypted snapshot will be encrypted as well.
EBS volume created based on unencrypted snapshot will be unencrypted as well.

#### Exploring Elastic Load Balancer
- Application Load Balancers - HTTP/HTTPS, it supports intelligent load balancing and routing requests to a specific web server based on type of requests
- Netword Load balancers - operates in Transport layer of ISO/OSI network model, provides high performance load balancing for TCP traffic
- Classic Load Balancers - legacy option supporting both HTTP/HTTPS and TCP
- Gateway Load Balancers - provides load balancing for 3rd party virtual appliances, like firewalls etc.

If it's needed to find address or original caller, there's need to look for this value in `X-Forwarded-For` header.

#### AWS CLI
Good practices:
- Always give your users the minimum amount of access required to do their job.
- Use groups to manage permissions.
- Secret access key can be seen only once, when it's lost there's need to generate new pair of access key + secret access key
- Don't share key pairs!

CLI operation page size can be controlled by `--page-size` option. CLI will still fetch full list, but in smaller batches what will help avoiding `timed out` errors. Maximum amount of retrieved items can be controlled by `--max-items`.