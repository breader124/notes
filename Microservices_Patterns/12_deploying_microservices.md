# Chapter 12 - Deploying microservices

Production env must implement 4 following capabilities:
- service management interface - responsible for creating, updating and configuring services, has GUI
- runtime service management - responsible for ensuring that desired number of services are working fine and restarting some in case of any failures
- monitoring - gives devs insight into what services are doing, their logs and metrics
- request routing - routes requests from users to services

## Deploying services using language-specific packaing format

**Pattern: Language-specific packaging format** - Deploy a language-specific package into production.

Benefits:
- fast deployment - just uploading e.g. JAR file to the server
- efficient resource utilization, especially when running multiple instances on the same machine or within the same process - Java apps might e.g. use the same JVM or Tomcat instance

Drawbacks:
- lack of encapsulation of technology stack - operations need to know how to prepare environment for apps' deployment, for example they need to know which JDK they should install
- no ability to constrain the resources consumed by a service instance - one service utilizing too much resources might cause starvation of other ones
- lack of isolation when running multiple service instances on the same machine - malfunctioning service might affect other ones
- automatically determining where to place service instances is challenging - it's challenging to determine which machine has enough resources to handle the instance that is currently being dpeloyed

## Deploying service using virtual machines

**Pattern: Deploy a service as a VM** - Deploy service packaged as VM images into production. Each service instance is a VM.

Benefits:
- the VM images encapsulates the technology stack - everything is packed into the VM, there's no chance for the case when we need to align common version of JDK
- isloated service instances - each VM has some resources assigned and there's no option for one service to affect another one
- uses mature cloud infrastructure - VMs are battle-tested by every existing cloud provider

Drawbacks:
- less-efficient resource utilization - there's an overhead of running entire operating system instead of only a service
- relatively slow deployments - deploying VM means that there's need to build it and then run what includes booting OS
- system administration overhead - using VMs it's almost unevitable that some help from system administrator side will be needed

## Deploying service using containers

**Pattern: Deploy a service as a container** - Deploy service packaged as container images into production. Each service instance is a container.

*container image* - filesystem image consisting of the application and any software required to run the service

Benefits:
- encapsulation of the technology stack in which the API for managing your services become the container API
- service instances are isolated
- service instance's resources are constrained
- while having almost the same set of benefits in comparison to VMs, containers are way more light than VMs

Drawbacks:
- need to administer the infrastructure running the containers unless some solution is chosen to take care of it

## Deploying the application with Kubernetes

Kubernetes is a Docker orchastration framework. It treats set of machines running Docker as a pool of resources.

Master machine of k8s runs following components:
- API server - REST API for deploying and managing services, used by *kubectl* for example
- Etcd - A key-value NoSQL database that stores the cluster data
- Scheduler - Selects a node to run a pod
- Controller manager - Runs the controllers, which ensures that the state of the cluster matches the intended state

Node machine of k8s runs following components:
- Kubelet - Created and manages the pods running on the node
- Kube-proxy - Manages networking, including load balancing across pods
- Pods - The application services

Key k8s *objects*:
- Pod - basic unit of deployment in Kubernetes, it consists of one or more containers that share an IP address and storage volumes, most commonly it consists of one container running JVM, but it also happens that there are some more *sidecar* containers running along the main one
- Deployment - declarative specification of a pod, it's a controller that ensures that the desired number of instances of the pod are running at all times
- Service - provides clients of an application service with a static/stable network location, by default service is accessible only from inside of the cluster, but it's also possible to expose them to external world
- ConfigMap - a named collection of name-value pairs that defines the externalized configuration for one or more application services

With k8s it's not possible to achieve zero-time deployment at all. For that purpose we can utilise service mesh, e.g. in the form of Istio. With proper configuration, Istio creates control plane and data plane. Control plane is responsible for controlling data plane, setting rules for it and taking care of telemetry (creating and exposing). Data plane consists of Envoy containers running in the same pods as applications. Each pod has one additional Envoy container inside. Entire traffic goes through this additional layer. This way it's possible to finish the deployment of new version of the service and then start canary deployment. That means, reconfiguring the traffic the way that only part of it goes to the new version of an app. After subsequent confirmations that everything is working as intended, more and more traffic is being redirected to the new version to finally redirect it all and make a decision to shutdown old pods running old versions of the app that is currently not handling any traffic.

**Pattern: Sidecar** - Implement cross-cutting concerns in a sidecar process or container that runs alongside the service instance.

## Deploying services using the Serverless deployment pattern

**Pattern: Serverless deployment** - Deploy services using a serverless deployment mechanism provided by a public cloud.

As using serverless deployments might be seen as very appealing option because of lacking overhead of managing infrastructure, there's need to remember that it also forces event based programming model, which might not suit all use cases. After all, this option (serverless) always should be considered first.
