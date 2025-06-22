

Study / flashcards for

* eager vs lazy loading
* annotations for generating a UUID
* what are the differences for one to one, many to one, many to many and one to many
* Joining columns in tables
* primary keys and foreign keys
* What is the process for JWT?
* What is the process for RBAC? What is it?
* Why do we use DTOs instead of Entities
* Why do we use the ElementCollection<Enum>
* Differences between RBAC, ABAC, ACL


```sql
CREATE TABLE users (
  id VARCHAR(36) PRIMARY KEY,
  email VARCHAR(255),
  password VARCHAR(255)
);

CREATE TABLE bills (
  id VARCHAR(36) PRIMARY KEY,
  name VARCHAR(255),
  user_id VARCHAR(36),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

```

You use:

Table	id	Purpose
users	Unique per user	Primary key for user
bills	Unique per bill	Primary key for bill
bills.user_id	Foreign key	Connects bill to user

So: Each table has its own ID, but you use foreign keys (like user_id) to relate them.

RBAC

Model	Based On	Use Case
RBAC	User roles (e.g., ADMIN, USER)	Simpler apps or admin portals
ABAC	Attributes (e.g., user.department == "HR")	Fine-grained control (e.g., SaaS apps)
ACL	Access lists per resource	Per-record security (e.g., Google Docs sharing)