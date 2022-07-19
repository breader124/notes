**IAM is universal service, not regional.** 

Root account is created after first set up of AWS account. 
Newly created users:
- don't have any permissions assigned
- they have access key (can be seen anytime) and secret key (can be seen only once) generated, but don't have console access

IAM consists of:
- users
- groups
- roles

[IAM policy simulator](https://policysim.aws.amazon.com/home/index.jsp?#) allows to:
- test IAM permissions before committing them to production
- validate that the policy works as expected
- test policies already attached to existing users -> great for troubleshooting

Good practices:
- always use MFA for root account
- utilise password rotation mechanism