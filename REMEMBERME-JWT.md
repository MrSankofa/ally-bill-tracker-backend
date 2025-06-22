

Study Flashcards for:

* How are beans created in Spring and how are they used?
* What is the UserDetails class by default?
* Why should we write our own UserDetails Class?
* How does BCryptPassEncoder work?
*  Why do we need a securityConfig configuration been?
* How does the securityCOnfig bean work with the JwtAuthenticationFilter?
* How does the securityFilterChain intercept our requests and check it for authentication? and validlity?
* How are sessions created? 
* What is the UserNamePasswordAuthenticationFilter class? How does it work with our request interception?
* How do you create a jwtAuthenticationFilter? Why do we need one and the SecurityConfig > SecurityFilterChain isn't enough without it?
* How do you keep the API paths DRY?
* Should generate a token from the email or the userId if the userId is not the email?
* How do you use TDD when implementing a jwtFilter? Red - implement - pass (what's the technical way of saying that)
  * you'll need to write a springBootTest
  * test if the token includes the email and the roles
  * use the supersecretsigning key to help parse the token with the io.jsonwebtoken library methods (setSingingKey, parseClaimsJws, getBody)
  * use the result of getBody to get the subject which should be the user name, the roles will be in a list. Seems like there is a map/object that stores the subject and rbac roles
* How does token store the username and roles in itself?
  * in the jwtService there is method called generateToken.
  * it uses the io.jsonWebToken library which has builder class that takes in the subject with a method called setSubject
  * it has a method called claim which allows you to specify how the roles will be stored .claim("roles", roles)
  * it has a method called setIssueAt, setExpiration, and setWith to set the signInKey and specify the algorithm used to secure it SignatureAlgorithm.HS256
  * Also how to make it compact
* How to write a custom UserDetails?
* When should we implement the isAccount expired, nonLocked, credentialsExpired, isEnabled and how?
* Be able to breakdown what is involved in a jwtAuthFilter and the different options you can choose like OncePerRequestFilter, etc
  * jwt Service
    * sigining key secret
    * expiration duration
    * a method to encode the jwtSecret with a hash. How it it using hmacShaKey amd converting the secret string in to bytes to create the sign in key
    * a method to generate the token
    * a method to extract the username from the token
    * a method to get the claims from the token?
    * a method to determine if the token is valid
      * a token is valid if you can get the username out of it and it matches what the userDetails class has for userName
      * and if the token has not expired
    * a method to determine if a token has expired
  * UserDetailsService
    * Where does the UserDetailsService come from? How does it work? How did it get connected with our data?
    * spring security core has it and spring manages
  * a method to doFilterInternal? 
    * it receives the request (HttpServletRequest), (HttpServletResponse) and FilterChain
    * it checks the request if it has auth setting (authHeader), it should be a string that starts with Bearer
      * if the authHeader is null or doesn't start with Bearer, filter it (don't allow it hit the api) send a response and don't do anything else
      * if it does have the things above, get the jwt and extract the username
      * check if the username is valid (not null) and the username is authenticated
        * load the user name into the userDetails class by using the UserDetailsService
        * use the jwtService to determine if the token is valid
        * when valid, add the UsernamePasswordAuthenticationToken to the security Context holder
* Understand what claims is in an authentication process? It is used a lot
  * a claim is a piece of infomation that the token asserts about the user (or client app)
  * claims could be thought of as a badge that a user shows when trying to access a system
  * It's basically like a security guard, holding someone (a request) at the door and have a list of claims that someone say they have certain properties or attributes
  * jwt frankly uses claims
* How does the userDetails get username and store it so we can determine if a token is valid?
* What is a filterChain?
* What does jwt standfor? Is it the token itself
  * jSON web token
  * JSON: The token uses JavaScript Object Notation (JSON) to encode its data. 
  * Web: It’s designed to work seamlessly across web technologies (e.g., HTTP headers, URLs). 
  * Token: It's a string that represents a trusted exchange of information — often for authentication and authorization.
* What is authentication/ what is authorization?
  * authentication - who are you
  * authorization - what you can do
* What are authorities in auth world it is referenced a lot?
* What is the securityContextHolder? How does it play into this process
* RBAC stands for?
  * role based access control


jwt security analogy

Imagine a security guard at the door of a building:

You hand them a badge (JWT).

The badge contains claims like:

sub: "brett@example.com" → your identity

roles: ["USER", "ADMIN"] → your level of access

exp: 6:00pm → when your badge expires

The guard checks:

* Is the badge valid (signature)?

* Is it expired?

* What access does it claim you have?

* Do your claims entitle you to enter this door?
 
 
Why JWTs use claims

Because JWTs are self-contained, they must store all info the API needs to:

Know who the user is.

Know what the user can access.

Know when the token expires.

Know how it was issued.

JWT structure

Example JWT Structure (3 parts, dot-separated):
```
xxxxx.yyyyy.zzzzz
```
Header (Base64-encoded JSON):

```json
{
"alg": "HS256",
"typ": "JWT"
}
```
Payload / Claims (Base64-encoded JSON):

```json
{
"sub": "user@example.com",
"roles": ["USER"],
"exp": 1712345678
}
```
Signature (HMAC or RSA):

A cryptographic hash of the header + payload, using a secret key.

Verifies token wasn't tampered with.

Can You Decode a JWT with base64decode.org?
Partially — yes.
JWTs consist of three parts separated by dots (.):

```
<Header>.<Payload>.<Signature>
```
Each of these is Base64URL encoded.
 
What You Can Decode:
The Header and Payload can be decoded easily using a site like https://www.base64decode.org (or jwt.io).

This will reveal things like:

alg (algorithm used)

sub (subject or user ID/email)

exp (expiration timestamp)

custom claims like "roles": ["USER", "ADMIN"]

What You Cannot Decode:
The Signature is not just base64 encoded — it’s a hashed result using a secret key. It cannot be decoded unless you:

Know the exact secret key (supersecretkey123... in your code)

Know the hashing algorithm used (HS256 in your code)

This is what guarantees token integrity. If someone tampers with the header or payload, the signature validation will fail.

Why the Hash Prevents Forgery
The signature is generated as:

```sccs
HMAC_SHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), secretKey)
```
Even if someone decodes the payload and modifies the data (e.g., changing their "role" from "USER" to "ADMIN"), they:

Cannot recreate a valid signature

Will fail verification inside your backend (jwtService.isTokenValid(...))

Does the Expiration Help?
Yes. Even if someone somehow brute-forced the secret (which is infeasible with proper key length), the token would be invalid after 1 hour.


Check for understanding question:

So the header and payload in this case is not private information? We're not concerned about people know this data? We use the signature to specify who has access to our API and the only way they can get it is by requesting it? So random requests that hit our routes would be sent a 401?

cfu answer:
Header and Payload: Not Confidential
Yes — the JWT header and payload are not encrypted, just Base64URL-encoded. That means:

Anyone who gets a JWT can decode and read the data inside the header and payload.

Examples of non-sensitive claims in the payload:

sub: the user ID or email

roles: user roles like "USER", "ADMIN"

iat, exp: issued at / expiration times

Therefore: Never put passwords, SSNs, or other sensitive information inside the payload.

The Signature: Ensures Integrity & Authenticity
The signature is what:

Prevents tampering (it proves the token wasn’t altered after issuing)

Proves the token was generated by your backend using your secret key

The signature can only be verified if the server has the original secret key (you have this stored in your JwtService).

How We Use JWT in Securing the API
When a client sends a JWT in the Authorization: Bearer <token> header:

You extract it in your JwtAuthenticationFilter

You decode the header and payload

You re-create the signature using the header + payload and your secret

If the signature matches: trust the data inside

Otherwise:  reject with 401 Unauthorized

So to answer your specific points:
Question	Answer
Is the payload private?	No. It's readable. So don't put private data inside.
Is the signature what secures access?	Yes. It proves the token is legitimate.
How do users get a valid token?	By successfully logging in or registering via your /auth endpoints.
What happens if someone makes a request without a valid token?	Your JwtAuthenticationFilter blocks it and the user gets a 401 Unauthorized.

Bonus Tip:
If you ever need encrypted payloads, you’d look into JWE (JSON Web Encryption) — a different, more advanced format. But for most use cases (like yours), signed JWT (JWS) is secure and appropriate as long as:

The secret is long and kept safe

You don’t store sensitive data in the token
