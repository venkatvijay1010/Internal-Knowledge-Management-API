# AI Engineer Portfolio Project Plan

## Codebase Analysis Summary

**Industry**: Multi-Line Insurance (Health, Motor, Life, Travel)  
**Platform**: GO Digit Insurance Microservices  
**Core Entities**: Policies, Claims, Quotes, Enrollments, Members, Endorsements  
**Architecture**: Layered microservices with Strategy pattern, AOP, Async processing, Redis caching  
**Engineering Strengths**: Security hardening, data validation pipelines, async bulk processing, multi-tenant product handling, Kafka event streaming

---

## 1. Project Recommendation

**Project Name**: PolicyMind — Agentic RAG for Insurance Claims Research

**Why This Is The Best Choice**:
- Direct domain alignment: Your codebase handles health insurance policies, claims, quotes, and member enrollments — PolicyMind solves the exact problem of researching coverage across policy documents and claims history
- Architecture translation: Your Strategy pattern for validation maps directly to agent tool-routing; your async processing maps to parallel agent execution
- Schema familiarity: Entities like `EnrolmentDetails`, `QuoteMemberInfo`, `GroupActuarialHealthClaims` translate 1:1 to synthetic policy/claims data
- Highest interview signal: Agentic RAG is the #1 skill companies probe for Applied AI roles

**Resume Value**:
- Demonstrates end-to-end AI system design, not just LLM API calls
- Shows hybrid deterministic + LLM decision-making (your validation strategy pattern in AI form)
- Proves you can bridge domain expertise into AI applications — exactly what enterprise AI teams need

**AI Skills Demonstrated**:

| Skill | Signal Level |
|-------|-------------|
| Agentic Tool Routing | HIGH — core interview topic |
| RAG Pipeline Design | HIGH — production RAG is complex |
| Hybrid Search (Vector + BM25) | MEDIUM-HIGH — shows retrieval depth |
| Citation-Grounded Generation | HIGH — prevents hallucination |
| Structured Output Enforcement | MEDIUM — production necessity |
| LLM Evaluation (RAGAS) | HIGH — separates senior from mid |
| Observability & Tracing | MEDIUM — production readiness |

---

## 2. Dataset Strategy

### Entity Mapping: Your Codebase → Synthetic Data

| Your Entity | Synthetic Equivalent | Public Data Source |
|-------------|---------------------|-------------------|
| `EnrolmentDetails` | Policy Document | Synthetic policy PDFs |
| `QuoteMemberInfo` | Member Profile | Faker + Synthetic |
| `GroupActuarialHealthClaims` | Claim Record | CMS Medicare claims format |
| `ICDCodeMaster` | Diagnosis Codes | Public ICD-10 codes |
| `HealthParamMaster` | Coverage Parameters | Synthetic |
| `TariffPolicy` / `TariffPremium` | Rate Tables | Synthetic |
| `EndorsementRequest` | Policy Amendments | Synthetic |

### Data Source Options

1. **Policy Documents (Unstructured)**:
   - Generate 50-100 synthetic health insurance policy PDFs using templates
   - Include: coverage limits, exclusions, deductibles, waiting periods, co-pay structures
   - Source: OpenAI/Claude to generate realistic policy language

2. **Claims History (Structured)**:
   - CMS Medicare Synthetic Claims dataset (public domain)
   - Or generate synthetic claims matching your `GroupActuarialHealthClaims` schema
   - 10,000-50,000 records with: claim_id, policy_id, diagnosis_code, amount, status, date

3. **ICD/Diagnosis Codes**:
   - Public ICD-10-CM codes (free from CMS)
   - Map to your existing `ICDCodeMaster` structure

### Expected Schema

```sql
policies
├── policy_id (PK)
├── policy_number
├── product_type (health/motor)
├── coverage_limit
├── deductible
├── effective_date
├── expiry_date
└── document_embedding (vector)

claims
├── claim_id (PK)
├── policy_id (FK)
├── diagnosis_code
├── treatment_type
├── claim_amount
├── status
├── claim_date
└── adjuster_notes

policy_chunks
├── chunk_id (PK)
├── policy_id (FK)
├── section_type (coverage/exclusion/limits)
├── content
├── embedding (vector)
└── page_number

members
├── member_id (PK)
├── policy_id (FK)
├── name
├── relationship
├── age
└── pre_existing_conditions
```

### Data Generation Strategy

- Week 1: Use Claude/GPT-4 to generate 50 realistic policy documents with varied coverage structures
- Seed from your actual field names: `sumInsuredAmt`, `deductibleAmount`, `waitingPeriodDays`, `preExistingDiseaseCoverage`
- Generate claims data with realistic distributions matching your `GroupActuarialHealthClaims` patterns

---

## 3. Technology Mapping

### Current Strengths → AI Stack Equivalents

| Java/Spring Boot Concept | Python/AI Equivalent | Translation |
|-------------------------|---------------------|-------------|
| `@RestController` | FastAPI `@router` | Identical pattern, async by default |
| `@Service` layer | Service classes | Same separation of concerns |
| `@Repository` + JPA | SQLAlchemy + Repository pattern | ORM patterns identical |
| Strategy Pattern (Validation) | LangGraph Tool Routing | Your validation strategies become agent tools |
| `@Async` + ThreadPoolExecutor | `asyncio` + concurrent execution | Similar but different syntax |
| Redis caching | Redis + semantic cache | Add embedding-based cache |
| Kafka events | LangGraph state transitions | Event-driven → state machine |
| `@ControllerAdvice` exception handling | FastAPI exception handlers | Identical concept |
| Mockito unit tests | pytest + mock | Same TDD patterns |

### New Skills To Learn

| AI Component | What To Learn | Learning Curve |
|--------------|---------------|----------------|
| **LangGraph** | State machines for agent orchestration; your Strategy pattern becomes a graph of tool nodes | 1 week — concepts transfer from your async patterns |
| **Vector Embeddings** | Dense representations of text; think of it as semantic indexing vs. your keyword validation | 2-3 days — conceptually simple |
| **pgvector** | PostgreSQL extension for vector similarity; just a new column type | 1 day — you know PostgreSQL |
| **RAG Pipeline** | Retrieve → Augment → Generate; similar to your "fetch policy → validate → respond" patterns | 1 week — core of this project |
| **Hybrid Search** | Combine vector similarity + BM25 keyword; like combining your `pincodeValidation` + `semanticValidation` | 3-4 days |
| **Structured Output** | Force LLM to return Pydantic schemas; your DTOs enforced on LLM output | 2 days — you understand DTO contracts |
| **RAGAS Evaluation** | Measure RAG quality; similar to your audit trails but for AI accuracy | 3-4 days |
| **LangSmith/Phoenix** | Observability for LLM apps; like your `LoggerAspects` + Actuator but for AI | 2 days |

---

## 4. System Design

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                         PolicyMind System                          │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌──────────┐    ┌──────────────────────────────────────────────┐  │
│  │  FastAPI │───▶│            LangGraph Orchestrator            │  │
│  │   /ask   │    │  ┌────────────────────────────────────────┐  │  │
│  └──────────┘    │  │           Query Classifier              │  │  │
│                  │  │  (document_qa / claims_sql / hybrid)    │  │  │
│                  │  └───────────────┬────────────────────────┘  │  │
│                  │                  │                            │  │
│                  │    ┌─────────────┼─────────────┐              │  │
│                  │    ▼             ▼             ▼              │  │
│                  │ ┌──────┐   ┌──────────┐   ┌─────────┐         │  │
│                  │ │ RAG  │   │   SQL    │   │ Hybrid  │         │  │
│                  │ │Agent │   │  Agent   │   │  Agent  │         │  │
│                  │ └──┬───┘   └────┬─────┘   └────┬────┘         │  │
│                  │    │            │              │              │  │
│                  └────┼────────────┼──────────────┼──────────────┘  │
│                       │            │              │                 │
│         ┌─────────────┼────────────┼──────────────┼───────────┐     │
│         │             ▼            ▼              ▼           │     │
│         │  ┌──────────────┐  ┌───────────┐  ┌────────────┐    │     │
│         │  │   pgvector   │  │ PostgreSQL│  │   Both +   │    │     │
│         │  │  (policies)  │  │  (claims) │  │  Citation  │    │     │
│         │  └──────────────┘  └───────────┘  └────────────┘    │     │
│         │              Data Layer                             │     │
│         └─────────────────────────────────────────────────────┘     │
│                                                                     │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │                    Supporting Services                      │    │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐   │    │
│  │  │  Evaluation  │  │ Observability│  │   Deterministic  │   │    │
│  │  │   (RAGAS)    │  │  (LangSmith) │  │   Math Engine    │   │    │
│  │  └──────────────┘  └──────────────┘  └──────────────────┘   │    │
│  └─────────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────────┘
```

### Major Services

| Service | Responsibility |
|---------|---------------|
| **Query Classifier** | Route incoming questions to appropriate agent (like your product-type routing) |
| **RAG Agent** | Vector search over policy documents, retrieve relevant chunks, generate cited answers |
| **SQL Agent** | Natural language → SQL over claims database, with execution feedback loop |
| **Hybrid Agent** | Orchestrate both when question spans documents + data |
| **Citation Engine** | Attach source references to every claim (policy section, page, claim ID) |
| **Math Engine** | Deterministic calculations for coverage limits, deductibles (never trust LLM math) |
| **Evaluation Service** | Run RAGAS metrics on query-response pairs |
| **Observability** | Trace every LLM call, tool invocation, retrieval |

### Data Flow

```
User Query → FastAPI → Query Classification
                           │
         ┌─────────────────┼─────────────────┐
         ▼                 ▼                 ▼
   "What's my          "Total claims      "Is knee surgery
    coverage for        in Q2 2024?"       covered under
    surgery?"                               policy P-1234?"
         │                 │                 │
         ▼                 ▼                 ▼
    RAG Agent          SQL Agent        Hybrid Agent
         │                 │                 │
         ▼                 ▼                 ▼
   Vector Search      Generate SQL       Both paths
   + Reranking        + Execute          + Merge
         │                 │                 │
         └─────────────────┼─────────────────┘
                           ▼
                  Response Synthesizer
                  (with citations)
                           │
                           ▼
                  Deterministic Checks
                  (coverage math)
                           │
                           ▼
                  Final Response + Traces
```

### Agent Flow (LangGraph State Machine)

```
START
  │
  ▼
┌─────────────────┐
│ classify_query  │ ─────────────────────────────────────┐
└────────┬────────┘                                      │
         │                                               │
         ├──── document_qa ────▶ ┌──────────────────┐    │
         │                       │   retrieve_docs  │    │
         │                       └────────┬─────────┘    │
         │                                │              │
         │                       ┌────────▼─────────┐    │
         │                       │   generate_answer│    │
         │                       └────────┬─────────┘    │
         │                                │              │
         ├──── claims_sql ──────▶ ┌───────▼────────┐     │
         │                        │  generate_sql  │     │
         │                        └───────┬────────┘     │
         │                                │              │
         │                        ┌───────▼────────┐     │
         │                        │  execute_sql   │◀────┤ retry on error
         │                        └───────┬────────┘     │
         │                                │              │
         │                        ┌───────▼────────┐     │
         │                        │ explain_results│     │
         │                        └───────┬────────┘     │
         │                                │              │
         └──── hybrid ──────────▶ (both paths + merge)   │
                                          │              │
                                 ┌────────▼─────────┐    │
                                 │  add_citations   │    │
                                 └────────┬─────────┘    │
                                          │              │
                                 ┌────────▼─────────┐    │
                                 │ deterministic_math│   │
                                 └────────┬─────────┘    │
                                          │              │
                                 ┌────────▼─────────┐    │
                                 │      END         │    │
                                 └──────────────────┘    │
```

### Retrieval Flow

```
Query: "Is knee replacement covered?"
                │
                ▼
        ┌───────────────┐
        │ Embed Query   │  (text-embedding-3-small or local)
        └───────┬───────┘
                │
                ▼
        ┌───────────────────────────────────────────┐
        │          Hybrid Search                    │
        │  ┌─────────────────┐  ┌────────────────┐  │
        │  │  Vector Search  │  │  BM25 Keyword  │  │
        │  │  (pgvector)     │  │  (full-text)   │  │
        │  └────────┬────────┘  └───────┬────────┘  │
        │           │                   │           │
        │           └─────────┬─────────┘           │
        │                     ▼                     │
        │           ┌─────────────────┐             │
        │           │  RRF Fusion     │             │
        │           │  (rank merge)   │             │
        │           └────────┬────────┘             │
        └────────────────────┼──────────────────────┘
                             │
                             ▼
                    ┌────────────────┐
                    │   Reranker     │  (Cohere or cross-encoder)
                    │   Top-K = 5    │
                    └───────┬────────┘
                            │
                            ▼
                    Retrieved Chunks + Metadata
                    (section_type, page_number, policy_id)
```

### Evaluation Flow

```
┌────────────────────────────────────────────────────────────────┐
│                    Evaluation Pipeline                        │
├────────────────────────────────────────────────────────────────┤
│                                                                │
│  Test Dataset (50-100 Q&A pairs with ground truth)             │
│         │                                                      │
│         ▼                                                      │
│  ┌──────────────┐                                              │
│  │ Run Pipeline │  (same query → PolicyMind → response)        │
│  └──────┬───────┘                                              │
│         │                                                      │
│         ▼                                                      │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │              RAGAS Metrics                               │  │
│  │  ┌────────────────┐  ┌────────────────┐                  │  │
│  │  │ Faithfulness   │  │ Answer         │                  │  │
│  │  │ (grounded in   │  │ Relevance      │                  │  │
│  │  │  context?)     │  │ (answers the   │                  │  │
│  │  │                │  │  question?)    │                  │  │
│  │  └────────────────┘  └────────────────┘                  │  │
│  │  ┌────────────────┐  ┌────────────────┐                  │  │
│  │  │ Context        │  │ Context        │                  │  │
│  │  │ Precision      │  │ Recall         │                  │  │
│  │  │ (relevant      │  │ (all relevant  │                  │  │
│  │  │  retrieved?)   │  │  retrieved?)   │                  │  │
│  │  └────────────────┘  └────────────────┘                  │  │
│  └──────────────────────────────────────────────────────────┘  │
│         │                                                      │
│         ▼                                                      │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │              Operational Metrics                         │  │
│  │  ┌────────────┐  ┌────────────┐  ┌────────────────┐      │  │
│  │  │ Latency    │  │ Cost per   │  │ Hallucination  │      │  │
│  │  │ p50/p95    │  │ Query      │  │ Rate           │      │  │
│  │  └────────────┘  └────────────┘  └────────────────┘      │  │
│  └──────────────────────────────────────────────────────────┘  │
│         │                                                      │
│         ▼                                                      │
│  Evaluation Report (JSON + Dashboard)                          │
│                                                                │
└────────────────────────────────────────────────────────────────┘
```

---

## 5. Week-by-Week Plan

### Week 1: Foundation + RAG Pipeline

**Goals**:
- Set up Python/FastAPI project structure
- Ingest synthetic policy documents
- Implement basic RAG retrieval

**Deliverables**:
- [ ] FastAPI skeleton with health endpoint
- [ ] PostgreSQL + pgvector schema deployed
- [ ] 20 synthetic policy documents generated and chunked
- [ ] Basic vector search returning relevant chunks
- [ ] Simple `/ask` endpoint answering document questions

**Demo Milestone**: Query "What is the deductible for surgery?" and get a relevant chunk with page citation.

---

### Week 2: Agentic Routing + SQL Agent

**Goals**:
- Implement LangGraph orchestrator
- Add query classification
- Build SQL agent for claims queries

**Deliverables**:
- [ ] LangGraph state machine with 3 paths (rag/sql/hybrid)
- [ ] Query classifier distinguishing document vs. data questions
- [ ] SQL agent with self-correction (retry on error)
- [ ] Synthetic claims table with 5,000 records
- [ ] Deterministic math engine for coverage calculations

**Demo Milestone**: "What was the total claim amount for policy P-1234 in 2024?" returns correct SQL-derived answer.

---

### Week 3: Hybrid Search + Citations + Observability

**Goals**:
- Implement hybrid vector + BM25 search
- Add citation grounding to all responses
- Integrate observability (LangSmith or Phoenix)

**Deliverables**:
- [ ] RRF fusion combining vector + keyword results
- [ ] Reranker integration (Cohere or cross-encoder)
- [ ] Every response includes [Source: Policy X, Section Y, Page Z]
- [ ] LangSmith traces for every query
- [ ] Latency metrics collection

**Demo Milestone**: "Is knee replacement surgery covered?" returns answer citing specific policy section and page.

---

### Week 4: Evaluation + Polish + Documentation

**Goals**:
- Implement RAGAS evaluation suite
- Create evaluation dashboard
- Write documentation and README

**Deliverables**:
- [ ] 50-question evaluation dataset with ground truth
- [ ] RAGAS metrics: faithfulness, relevance, precision, recall
- [ ] Comparison report: hybrid vs. vector-only ablation
- [ ] README with architecture diagram, setup instructions
- [ ] Docker Compose for one-command deployment
- [ ] 3-minute demo video

**Demo Milestone**: Run full evaluation, show metrics dashboard, explain trade-offs.

---

## 6. Clean Architecture Folder Structure

```
policymind/
├── domain/                     # Core business logic (no dependencies)
│   ├── entities/
│   │   ├── policy.py           # Policy, PolicyChunk domain objects
│   │   ├── claim.py            # Claim domain object
│   │   └── query.py            # Query, QueryResult value objects
│   ├── services/
│   │   ├── coverage_calculator.py  # Deterministic math (never LLM)
│   │   └── citation_builder.py     # Build source citations
│   └── interfaces/             # Abstract repos/ports
│       ├── policy_repository.py
│       └── claim_repository.py
│
├── application/                # Use cases / orchestration
│   ├── query_handler.py        # Main entry point for /ask
│   ├── agents/
│   │   ├── query_classifier.py # Route to rag/sql/hybrid
│   │   ├── rag_agent.py        # Document retrieval + generation
│   │   ├── sql_agent.py        # NL-to-SQL with retry
│   │   └── hybrid_agent.py     # Orchestrate both
│   └── graph/
│       └── orchestrator.py     # LangGraph state machine
│
├── infrastructure/             # External dependencies
│   ├── database/
│   │   ├── postgres.py         # SQLAlchemy engine
│   │   └── pgvector.py         # Vector operations
│   ├── llm/
│   │   ├── openai_client.py    # OpenAI wrapper
│   │   └── embeddings.py       # Embedding generation
│   ├── search/
│   │   ├── hybrid_search.py    # RRF fusion implementation
│   │   └── reranker.py         # Cohere/cross-encoder
│   └── observability/
│       ├── langsmith.py        # Tracing setup
│       └── metrics.py          # Prometheus metrics
│
├── api/                        # FastAPI layer
│   ├── main.py                 # App entry point
│   ├── routes/
│   │   ├── ask.py              # POST /ask endpoint
│   │   ├── health.py           # GET /health
│   │   └── eval.py             # POST /eval/run
│   └── schemas/
│       ├── request.py          # Pydantic request models
│       └── response.py         # Pydantic response models
│
├── evaluation/                 # RAGAS + custom metrics
│   ├── ragas_runner.py         # Run RAGAS evaluation
│   ├── metrics/
│   │   ├── faithfulness.py
│   │   ├── relevance.py
│   │   └── retrieval.py
│   ├── datasets/
│   │   └── eval_questions.json # Ground truth Q&A
│   └── reports/
│       └── generate_report.py
│
├── data/                       # Data generation + seeding
│   ├── generators/
│   │   ├── policy_generator.py # Synthetic policy docs
│   │   └── claims_generator.py # Synthetic claims data
│   ├── seed/
│   │   └── icd_codes.csv       # Public ICD-10 codes
│   └── documents/              # Generated policy PDFs
│
├── tests/
│   ├── unit/
│   │   ├── test_coverage_calculator.py
│   │   └── test_citation_builder.py
│   ├── integration/
│   │   ├── test_rag_agent.py
│   │   └── test_sql_agent.py
│   └── e2e/
│       └── test_full_query.py
│
├── docker-compose.yml
├── Dockerfile
├── pyproject.toml
├── README.md
└── .env.example
```

**Responsibilities**:
- **domain/**: Pure business logic, no external dependencies. Your coverage calculation rules live here.
- **application/**: Orchestrates domain + infrastructure. LangGraph state machine coordinates agents.
- **infrastructure/**: All external integrations (DB, LLM, search). Easy to swap providers.
- **api/**: FastAPI routes, request validation, response formatting. Thin layer.
- **evaluation/**: RAGAS integration, evaluation datasets, reporting.
- **tests/**: Unit (domain logic), Integration (agents), E2E (full queries).

---

## 7. Evaluation Framework

### Metrics Definition

| Metric | Definition | Measurement Method |
|--------|------------|-------------------|
| **Precision@K** | Of top K retrieved chunks, how many are relevant? | Human-labeled relevance judgments |
| **Recall@K** | Of all relevant chunks, how many are in top K? | Against ground-truth relevant set |
| **MRR** | Mean Reciprocal Rank of first relevant result | 1/rank of first relevant chunk |
| **Faithfulness** | Is the answer grounded in retrieved context? | RAGAS LLM-as-judge |
| **Answer Relevance** | Does the answer address the question? | RAGAS LLM-as-judge |
| **Context Precision** | Is retrieved context relevant to question? | RAGAS metric |
| **Context Recall** | Did retrieval find all needed information? | RAGAS metric |
| **Hallucination Rate** | % of claims not supported by any source | Manual audit + automated check |
| **Latency (p50/p95)** | Response time distribution | Instrumentation |
| **Cost per Query** | LLM tokens + embedding calls | Token counting |

### Target Metrics (Realistic for 4-Week Build)

| Metric | Target | Stretch Goal |
|--------|--------|--------------|
| Precision@5 | ≥ 0.70 | ≥ 0.80 |
| Recall@5 | ≥ 0.65 | ≥ 0.75 |
| MRR | ≥ 0.60 | ≥ 0.70 |
| Faithfulness | ≥ 0.85 | ≥ 0.92 |
| Answer Relevance | ≥ 0.80 | ≥ 0.88 |
| Hallucination Rate | ≤ 10% | ≤ 5% |
| Latency p50 | ≤ 3s | ≤ 2s |
| Latency p95 | ≤ 8s | ≤ 5s |
| Cost per Query | ≤ $0.05 | ≤ $0.03 |

### Evaluation Dataset Structure

```json
{
  "questions": [
    {
      "id": "Q001",
      "question": "What is the maximum coverage for knee replacement surgery?",
      "ground_truth_answer": "$50,000 per occurrence with $500 deductible",
      "relevant_chunk_ids": ["chunk_045", "chunk_046"],
      "query_type": "document_qa",
      "difficulty": "easy"
    },
    {
      "id": "Q002", 
      "question": "How many claims were filed in Q2 2024?",
      "ground_truth_answer": "1,247 claims totaling $3.2M",
      "relevant_tables": ["claims"],
      "query_type": "claims_sql",
      "difficulty": "medium"
    }
  ]
}
```

---

## 8. Resume Positioning

### Resume Bullet Points

**1. Technical Achievement**:
> Built an agentic RAG system for insurance claims research using LangGraph, combining vector retrieval (pgvector), SQL query generation, and deterministic coverage calculations, achieving 85% faithfulness and 70% retrieval precision on a 50-question evaluation benchmark.

**2. Architecture/Design**:
> Designed a hybrid search pipeline (vector + BM25) with Reciprocal Rank Fusion and reranking, reducing hallucination rate to <10% through citation-grounded generation and deterministic math for coverage calculations.

**3. Evaluation/Production Readiness**:
> Implemented end-to-end LLM evaluation using RAGAS (faithfulness, relevance, context precision), LangSmith observability, and latency tracking, establishing a reproducible evaluation workflow for RAG system iteration.

### LinkedIn Project Description

> **PolicyMind — Agentic RAG for Insurance Research**
>
> An AI-powered research assistant that answers insurance claims questions by intelligently routing between document retrieval (policy PDFs) and structured data queries (claims database). Built with Python, FastAPI, LangGraph, and PostgreSQL + pgvector.
>
> Key features:
> • Agentic tool routing: Classifies queries and orchestrates RAG, SQL, or hybrid workflows
> • Citation-grounded answers: Every claim links to source documents and page numbers
> • Self-correcting SQL: Executes queries, catches errors, and retries with context
> • Deterministic math: Coverage calculations never trust LLM arithmetic
> • Full evaluation suite: RAGAS metrics, LangSmith tracing, latency monitoring
>
> Technical highlights: Hybrid vector + BM25 search, RRF fusion, structured output enforcement, 85% faithfulness score.

### Interview Talking Points

1. **"Walk me through the architecture."**
   → Start with query classification (your Strategy pattern), show the LangGraph state machine, explain why deterministic math for coverage (LLMs fail at arithmetic), end with citation enforcement.

2. **"Why hybrid search instead of pure vector?"**
   → Vector search misses exact matches (policy numbers, ICD codes). BM25 catches those. RRF fusion gets best of both. Show the ablation metrics.

3. **"How do you prevent hallucination?"**
   → Three layers: (1) Citation requirement — every claim needs a source, (2) Faithfulness evaluation — RAGAS measures grounding, (3) Deterministic math — coverage limits calculated in code, not LLM.

4. **"How would you scale this?"**
   → Async ingestion (Celery), horizontal API scaling, read replicas for SQL, caching frequent queries (semantic cache with Redis).

5. **"What was the hardest part?"**
   → Getting query classification right — ambiguous questions could go either way. Solved with few-shot examples in the classifier prompt and a "hybrid" fallback for uncertain cases.

---

## 9. Scope Reduction

### Features to Cut (Over-Engineered for Solo Build)

| Over-Engineered Feature | Simpler Alternative |
|------------------------|---------------------|
| Multi-tenant policy isolation | Single-tenant demo with mock data |
| Real-time streaming responses | Standard request-response (add streaming later) |
| Advanced reranker fine-tuning | Use Cohere API or skip reranking entirely |
| Multi-modal document parsing (images) | Text-only PDF extraction |
| Kubernetes deployment | Docker Compose only |
| CI/CD pipeline | Manual deployment + README instructions |
| User authentication | API key or no auth for demo |
| Comprehensive logging infrastructure | LangSmith free tier only |
| Multiple LLM provider fallback | Single provider (OpenAI or Anthropic) |
| Complex caching layers | Simple in-memory cache for demo |
| Production error alerting | Log-based error tracking |

### Minimum Viable Demo

Focus on these core flows only:
1. Document Q&A: "What's covered?" → RAG retrieval → cited answer
2. Claims Query: "Total claims in Q2?" → SQL generation → execution → explanation
3. Evaluation: Run RAGAS on 50 questions, show metrics dashboard

Skip for MVP:
- Hybrid queries (add if time permits)
- Streaming
- Caching
- Authentication
- Advanced observability dashboards

---

## Summary

**Project**: PolicyMind — Agentic RAG for Insurance Claims Research

**Why Perfect Fit**: Direct domain alignment (insurance), architecture translation (Strategy → Tool Routing), maximum AI skill signal for Applied AI roles

**Core Differentiators**:
1. Agentic routing (not just RAG)
2. Self-correcting SQL agent
3. Citation-grounded generation
4. Deterministic math for business logic
5. Full RAGAS evaluation

**4-Week Timeline**: Foundation → Agents → Hybrid Search + Observability → Evaluation + Polish

**Target Outcome**: Demo-ready portfolio project with metrics proving production-grade AI engineering

---

## Appendix: Your Codebase Analysis

### Repositories Analyzed
- s_digithealth
- s_grouphealthquote
- s_digitcare
- s_motorcommonservice
- s_datavalidation
- s_datawarehouse
- s_digitplus

### Key Entities Found
- `EnrolmentDetails` (master-detail policies)
- `QuoteMemberInfo` (quote members)
- `GroupActuarialHealthClaims` (claims data)
- `ICDCodeMaster` (diagnosis codes)
- `EndorsementRequest` (policy modifications)
- `TariffPolicy` / `TariffPremium` (rate tables)

### Architecture Patterns Identified
- Strategy Pattern (validation chains)
- AOP (logging aspects)
- Async processing (@EnableAsync)
- Redis caching
- Kafka event streaming
- DTO/Entity separation
- Centralized exception handling

### Engineering Strengths
- Security hardening (encryption, SQL injection prevention)
- Data validation pipelines
- Multi-tenant product handling
- Comprehensive audit trails
- Cloud-native deployment (Docker, K8s, Helm)

---

*Generated: 2026-07-28*
*Based on analysis of GO Digit Insurance microservices codebase*
