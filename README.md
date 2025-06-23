# ally-bill-tracker-backend

# Updated Roadmap for Finance Tracker Development (Phases Reordered)

## Phase 1: Security-First Foundation (⏳ In Progress)

**Goal:** Secure the backend and lay groundwork for future extensibility

### Completed
- [x] Setup Spring Boot backend with JWT-based authentication
- [x] Configure `@SpringBootTest` for integration testing of endpoints

### Still To Do
- [ ] Implement RBAC using `@PreAuthorize` and Spring Security roles
- [ ] Use `.env` or `application.yml` for secret and config management
- [ ] Add certificate-based HTTPS support (optional for local dev)
- [ ] Setup structured logging using `logback-spring.xml`
- [ ] Prepare backend to integrate LogRocket (attach session IDs to logs)

---

## Phase 2: DevOps Integration (Next Priority)

**Goal:** Enable continuous delivery and secure deployments
- [ ] Create GitHub + GitLab CI/CD YAML pipelines
- [ ] Add AWS deploy scripts (Elastic Beanstalk or ECS Fargate)
- [ ] Store backend artifacts in a Nexus repository
- [ ] Use `.env` or AWS Secrets Manager for runtime secrets
- [ ] Add Spring Boot profiles for `dev`, `test`, and `prod`

---

## 💻 Phase 3: Frontend Shell with Enterprise Layout

**Goal:** Build maintainable, authenticated UI with modular components
- [ ] Scaffold React app using Vite + pnpm workspaces
- [ ] Implement React Router with public and private route protection
- [ ] Build UI using Styled Components and enterprise design
- [ ] Set up Storybook for documenting reusable UI components
- [ ] Integrate Redux slices for `bills`, `income`, and `auth` state
- [ ] Unit test interactive UI with `fireEvent` and Testing Library
- [ ] Begin integrating LogRocket (see Phase 4A)

---

## Phase 4: Enterprise Testing & Monitoring

### Phase 4A – *Frontend Monitoring & Debugging*

**Goal:** Capture and trace frontend behavior
- [ ] Add LogRocket to React app (`LogRocket.init()`)
- [ ] Use `LogRocket.identify()` with user ID/email post-login
- [ ] Add `ErrorBoundary` to surface runtime UI errors
- [ ] Confirm session links work in LogRocket dashboard
- [ ] Forward `logrocket-session-id` to backend in request headers

### Phase 4B – *Backend Observability & Spring Batch Features*

**Goal:** Expand backend capabilities and improve traceability
- [ ] Connect Spring Boot to Oracle SQL via Spring Data JPA
- [ ] Add Spring Batch for scheduled or bulk bill processing
- [ ] Use Spring AOP for logging/auditing key actions (e.g. logins, bill edits)
- [ ] Accept LogRocket session IDs in headers and attach to logs
- [ ] Deploy backend components to ECS or WebLogic
- [ ] Prepare for future microservice refactor (structure code for separation)

---

## Phase 5 (Later): Microservices Refactor

**Goal:** Split monolith into scalable microservices
- [ ] Extract Auth, Billing, and Reporting into separate services
- [ ] Use Spring Cloud Eureka or AWS service discovery
- [ ] Add API Gateway (e.g., Spring Cloud Gateway or AWS API Gateway)
- [ ] Use Kafka or SQS for async communication where needed