**Region** is a cluster of data centers

While choosing correct region for the service there's need to take into consideration:
- compliance - requirements set by law 
- proximity - closer service is to the customer, less latency we have
- available services - not all of regions have all services
- pricing - service's price may vary from region to region

Each region has many **availability zones** (usually 3, min 2, max 6). AZ is or more discrete data centers completely isolated from other AZs. AZs within one region are connected using ultra-low latency networking.

There are some global services in AWS, but most of them are region-scoped.

