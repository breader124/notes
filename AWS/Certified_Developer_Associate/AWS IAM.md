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

[IAM policy simulator](https://policysim.aws.amazon.com/home/index.jsp?#) allows to:
- test IAM permissions before committing them to production
- validate that the policy works as expected
- test policies already attached to existing users -> great for troubleshooting

### Password policy and MFA

IAM allows to:
- set password policy
- allow IAM users to change their passwords
- set password rolling policy
- prevent password re-use

MFA = password **you know** + security device **you own**
With MFA, stolen or hacked password doesn't mean compromised account.

MFA device options:
- virtual MFA device
- Unversal 2nd Factor (U2F) Security Key
- hardware MFA device

### How to access AWS?
- AWS Management Console -- protected by password + MFA
- AWS Command Line Interface -- protected by access keys, alternative to console
- AWS Software Development Kit -- protected by access keys, to be used in the code

**Access keys are secret!**

### IAM Roles
With IAM roles it's possible to assign poermissions to AWS services to use other AWS services.

IAM role can be created using console creation wizard.

### IAM Security Tools
There are 2 security tools:
- IAM Credentials Report - works on account level, reports all account's users and status of their credentials
- IAM Access Advisor - works on user level, shows service permissions granted to a user and when those services were last accesses

### Good practices
1. Don't use the root account
2. One physical user = One AWS user
3. Assign user to groups and assigne permissions to groups
4. Create a strong password policy
5. Use and enforce the use of MFA
6. Create and use Roles for giving permissions to AWS services
7. Use Access Keys for programmatic access (CLI/SDK)
8. Audit permissions using IAM Credentials Report
9. Never share IAM users and Access Keys!