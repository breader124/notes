### Overview

**IAM is universal service, not regional.** 

Root account is created after first set up of AWS account. **Don't share it**

Newly created users:
- don't have any permissions assigned
- they have access key (can be seen anytime) and secret key (can be seen only once) generated, but don't have console access

IAM consists of:
- users (may belong to many groups, it's not requirement)
- groups (consist of users)
- roles

After assigning policy to user or group, this policy defines what user or group is allowed to do.

**Least privilege principle** - don't give more permissions than a user needs

### Policies

Policy consists of
- Version
- Id
- Statement:
	- Sid (optional)
	- Effect - `Allow` / `Deny`
	- Principal - account/user/role to which this policy applies
	- Action - list of actions this policy allows or denies
	- Resource - list of resources to which the actions applies
	- Condition (optional)

Policy can be attached directly to the user (inline policy) or inherited from group.

Policy can be created using visual editor or just typed in as a JSON documents.

### MFA

Good practices:
- always use MFA for root account
- utilise password rotation mechanism

### Other

[IAM policy simulator](https://policysim.aws.amazon.com/home/index.jsp?#) allows to:
- test IAM permissions before committing them to production
- validate that the policy works as expected
- test policies already attached to existing users -> great for troubleshooting