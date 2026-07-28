# PolicyMind Lite — RAG for Insurance Policy Q&A
## 2-Week Project Plan for 1 YoE Engineers

---

# Why This Version?

| Full Version Problem | Lite Version Solution |
|---------------------|----------------------|
| LangGraph is overkill | Simple function calls |
| Can't explain hybrid search | Pure vector search (understandable) |
| Too many moving parts | Single RAG pipeline |
| 4 weeks to build | 2 weeks, polished |
| Interview suspicion | Confident explanation |

**This project is STILL impressive because:**
- 90% of companies don't have working RAG systems yet
- You're demonstrating Python + AI transition from Java
- Clean architecture shows engineering maturity
- Evaluation shows you think about quality, not just "it works"

---

# Project Summary

**Name:** PolicyMind Lite — Insurance Policy Q&A with RAG

**One-liner:** A document Q&A system that answers insurance policy questions with source citations using vector search and LLM generation.

**Tech Stack:**
- Python 3.11
- FastAPI
- PostgreSQL + pgvector
- OpenAI (embeddings + GPT-4)
- Docker Compose

**What it does:**
1. Upload insurance policy PDFs
2. Chunk and embed documents
3. Ask questions in natural language
4. Get answers with citations (page/section)

**What it DOESN'T do (intentionally):**
- No multi-agent orchestration
- No SQL agent
- No hybrid search
- No reranking
- No complex observability

---

# Week 1: Core RAG Pipeline

## Day 1-2: Project Setup

**Tasks:**
- [ ] Initialize Python project with `pyproject.toml`
- [ ] Set up FastAPI skeleton
- [ ] Configure PostgreSQL + pgvector with Docker
- [ ] Create database schema
- [ ] Write health check endpoint

**Deliverable:** `docker-compose up` runs the stack

**Schema:**
```sql
-- Simple schema for Lite version
CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE policies (
    id SERIAL PRIMARY KEY,
    policy_number VARCHAR(100) UNIQUE NOT NULL,
    policy_name VARCHAR(255),
    product_type VARCHAR(50),
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE chunks (
    id SERIAL PRIMARY KEY,
    policy_id INTEGER REFERENCES policies(id),
    content TEXT NOT NULL,
    section_name VARCHAR(255),
    page_number INTEGER,
    chunk_index INTEGER,
    embedding VECTOR(1536),  -- OpenAI ada-002 dimension
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ON chunks USING ivfflat (embedding vector_cosine_ops);
```

---

## Day 3-4: Document Ingestion

**Tasks:**
- [ ] PDF text extraction (PyPDF2)
- [ ] Text chunking (500 tokens, 50 overlap)
- [ ] OpenAI embedding generation
- [ ] Store chunks with embeddings in pgvector
- [ ] `/ingest` endpoint

**Code Structure:**
```python
# Simple ingestion - no fancy frameworks
async def ingest_policy(file: UploadFile, policy_number: str):
    # 1. Extract text from PDF
    text = extract_pdf_text(file)
    
    # 2. Split into chunks
    chunks = split_into_chunks(text, chunk_size=500, overlap=50)
    
    # 3. Generate embeddings
    embeddings = await get_embeddings([c.content for c in chunks])
    
    # 4. Store in database
    await store_chunks(policy_id, chunks, embeddings)
    
    return {"status": "success", "chunks_created": len(chunks)}
```

**Deliverable:** Upload a PDF, see chunks in database

---

## Day 5-6: Vector Search + Generation

**Tasks:**
- [ ] Implement vector similarity search
- [ ] Build prompt with retrieved context
- [ ] Call OpenAI for answer generation
- [ ] Add basic citations to response
- [ ] `/ask` endpoint

**Code Structure:**
```python
async def ask_question(question: str, policy_id: int = None):
    # 1. Embed the question
    question_embedding = await get_embedding(question)
    
    # 2. Vector search (top 5 chunks)
    relevant_chunks = await search_similar_chunks(
        embedding=question_embedding,
        policy_id=policy_id,
        top_k=5
    )
    
    # 3. Build prompt with context
    context = format_context(relevant_chunks)
    prompt = build_qa_prompt(question, context)
    
    # 4. Generate answer
    answer = await generate_answer(prompt)
    
    # 5. Add citations
    response = {
        "answer": answer,
        "sources": [
            {
                "policy": chunk.policy_number,
                "section": chunk.section_name,
                "page": chunk.page_number,
                "snippet": chunk.content[:200]
            }
            for chunk in relevant_chunks
        ]
    }
    
    return response
```

**Deliverable:** Ask a question, get answer with sources

---

## Day 7: Testing + Refinement

**Tasks:**
- [ ] Write unit tests for chunking logic
- [ ] Write integration tests for /ask endpoint
- [ ] Test with 3-5 sample policy documents
- [ ] Fix edge cases (empty results, long questions)
- [ ] Add error handling

**Deliverable:** All tests pass, handles edge cases

---

# Week 2: Evaluation + Polish

## Day 8-9: Evaluation Dataset

**Tasks:**
- [ ] Create 30 Q&A pairs with ground truth
- [ ] Mix of easy/medium/hard questions
- [ ] Store in JSON file
- [ ] Build evaluation runner

**Evaluation Dataset Format:**
```json
{
  "questions": [
    {
      "id": "Q001",
      "question": "What is the waiting period for pre-existing diseases?",
      "ground_truth": "36 months from policy inception",
      "difficulty": "easy",
      "relevant_section": "Waiting Periods"
    },
    {
      "id": "Q002",
      "question": "Is cosmetic surgery covered under this policy?",
      "ground_truth": "No, cosmetic surgery is listed under General Exclusions",
      "difficulty": "easy",
      "relevant_section": "Exclusions"
    },
    {
      "id": "Q003",
      "question": "What is the sub-limit for room rent in a network hospital?",
      "ground_truth": "1% of Sum Insured per day or actual, whichever is lower",
      "difficulty": "medium",
      "relevant_section": "Coverage Limits"
    }
  ]
}
```

**30 Question Categories (10 each):**

**Easy (10):**
- What is covered?
- What is excluded?
- What are the waiting periods?
- What documents are needed for claims?
- What is the policy period?

**Medium (10):**
- Sub-limit questions
- Co-payment conditions
- Network vs non-network differences
- Pre-authorization requirements
- Specific procedure coverage

**Hard (10):**
- Edge cases (maternity + complications)
- Combined coverage scenarios
- Exclusion exceptions
- Limit calculations
- Multi-condition questions

---

## Day 10-11: Metrics Implementation

**Tasks:**
- [ ] Implement Answer Accuracy (human judgment)
- [ ] Implement Retrieval Precision (are sources relevant?)
- [ ] Implement basic Faithfulness check
- [ ] Run evaluation on 30 questions
- [ ] Generate metrics report

**Simple Metrics (No RAGAS needed):**

```python
# Simple evaluation - no complex frameworks
def evaluate_answer(question, generated_answer, ground_truth, retrieved_chunks):
    metrics = {}
    
    # 1. Answer Similarity (simple)
    # Use OpenAI to judge if answer matches ground truth
    metrics["answer_correct"] = llm_judge_correctness(
        question, generated_answer, ground_truth
    )
    
    # 2. Retrieval Relevance (simple)
    # Check if any retrieved chunk contains key terms from ground truth
    key_terms = extract_key_terms(ground_truth)
    chunks_text = " ".join([c.content for c in retrieved_chunks])
    metrics["retrieval_hit"] = any(term in chunks_text for term in key_terms)
    
    # 3. Has Citation
    metrics["has_citation"] = len(retrieved_chunks) > 0
    
    return metrics

def run_evaluation(eval_dataset):
    results = []
    for item in eval_dataset["questions"]:
        response = ask_question(item["question"])
        metrics = evaluate_answer(
            item["question"],
            response["answer"],
            item["ground_truth"],
            response["sources"]
        )
        results.append({"id": item["id"], **metrics})
    
    # Aggregate
    total = len(results)
    accuracy = sum(r["answer_correct"] for r in results) / total
    retrieval_precision = sum(r["retrieval_hit"] for r in results) / total
    
    return {
        "total_questions": total,
        "accuracy": accuracy,
        "retrieval_precision": retrieval_precision,
        "results": results
    }
```

**Target Metrics:**
| Metric | Target |
|--------|--------|
| Answer Accuracy | ≥ 75% |
| Retrieval Precision | ≥ 70% |
| Has Citation | 100% |

---

## Day 12-13: Documentation + Demo

**Tasks:**
- [ ] Write comprehensive README
- [ ] Add architecture diagram
- [ ] Document API endpoints
- [ ] Create sample requests
- [ ] Record 2-minute demo video (optional)

**README Structure:**
```markdown
# PolicyMind Lite

Insurance policy Q&A system using RAG (Retrieval-Augmented Generation).

## Features
- Upload insurance policy PDFs
- Ask questions in natural language
- Get answers with source citations
- Evaluation framework with 30 test questions

## Architecture
[Simple diagram]

## Quick Start
docker-compose up
curl -X POST /ingest ...
curl -X POST /ask ...

## API Endpoints
- POST /ingest - Upload policy document
- POST /ask - Ask a question
- GET /eval - Run evaluation

## Evaluation Results
- Accuracy: 78%
- Retrieval Precision: 73%

## Tech Stack
- Python 3.11, FastAPI
- PostgreSQL + pgvector
- OpenAI API

## Future Improvements
- Add hybrid search (BM25 + vector)
- Add SQL agent for claims data
- Add observability with LangSmith
```

---

## Day 14: Final Polish

**Tasks:**
- [ ] Clean up code
- [ ] Add type hints everywhere
- [ ] Final Docker Compose test
- [ ] Push to GitHub
- [ ] Write LinkedIn post

---

# Folder Structure (Simplified)

```
policymind-lite/
├── app/
│   ├── __init__.py
│   ├── main.py              # FastAPI app
│   ├── config.py            # Settings
│   ├── database.py          # DB connection
│   │
│   ├── models/
│   │   ├── __init__.py
│   │   ├── policy.py        # SQLAlchemy models
│   │   └── schemas.py       # Pydantic schemas
│   │
│   ├── services/
│   │   ├── __init__.py
│   │   ├── ingestion.py     # PDF processing + chunking
│   │   ├── embeddings.py    # OpenAI embeddings
│   │   ├── search.py        # Vector search
│   │   └── generation.py    # Answer generation
│   │
│   └── routes/
│       ├── __init__.py
│       ├── ingest.py        # POST /ingest
│       ├── ask.py           # POST /ask
│       └── eval.py          # GET /eval
│
├── evaluation/
│   ├── dataset.json         # 30 Q&A pairs
│   ├── runner.py            # Evaluation script
│   └── results/             # Evaluation outputs
│
├── data/
│   └── sample_policies/     # Test PDFs
│
├── tests/
│   ├── test_chunking.py
│   ├── test_search.py
│   └── test_api.py
│
├── docker-compose.yml
├── Dockerfile
├── requirements.txt
├── README.md
└── .env.example
```

---

# Synthetic Data (Simplified)

## Generate 3-5 Policy Documents

Use this prompt with Claude/GPT-4:

```
Generate a simple health insurance policy document (2-3 pages) with these sections:

1. COVERAGE SUMMARY
   - In-patient hospitalization
   - Day care procedures (list 5)
   - Pre/post hospitalization (30/60 days)

2. EXCLUSIONS
   - List 10 common exclusions

3. WAITING PERIODS
   - Initial: 30 days
   - Specific diseases: 24 months
   - Pre-existing: 36 months

4. CLAIM PROCESS
   - Cashless procedure (3 steps)
   - Reimbursement procedure (3 steps)
   - Required documents list

5. LIMITS
   - Room rent: 1% of SI
   - ICU: 2% of SI
   - Ambulance: Rs 2000

Policy Number: POL-2024-001
Sum Insured: Rs 5,00,000
Policy Period: 1 year

Output as plain text that can be saved as PDF.
```

Generate 3-5 variations with different:
- Sum Insured amounts (3L, 5L, 10L)
- Waiting periods
- Exclusion lists
- Limit structures

---

# Interview Preparation

## Questions You WILL Be Asked

**1. "Why did you build this?"**
> "I wanted to transition from Java backend to AI engineering. Insurance is my domain, and policy lookup is a real pain point I've seen. RAG is the foundation of most enterprise AI applications, so I built a working system to understand it deeply."

**2. "Walk me through how RAG works."**
> "Three steps: First, I chunk the policy documents and create embeddings using OpenAI's ada-002 model. These go into pgvector. When a user asks a question, I embed their question, do a cosine similarity search to find the top 5 relevant chunks, then pass those chunks as context to GPT-4 with the original question. The model generates an answer grounded in that context."

**3. "Why pgvector instead of Pinecone/Weaviate?"**
> "For a project this size, pgvector keeps everything in one database. I already know PostgreSQL from my Java work. For production with millions of documents, I'd consider a dedicated vector DB, but for 50-100 documents, pgvector is simpler and sufficient."

**4. "What's your chunking strategy?"**
> "500 tokens with 50-token overlap. The overlap ensures I don't lose context at chunk boundaries. For insurance policies specifically, I also tried to respect section boundaries where possible."

**5. "How do you handle hallucination?"**
> "Two ways: First, I always include the source chunks in the response so users can verify. Second, my prompt explicitly tells the model to say 'I don't have information about this' if the answer isn't in the context. I tested this with questions outside the policy scope."

**6. "What would you improve?"**
> "Three things: 
> 1. Add BM25 keyword search alongside vector search — some queries like policy numbers need exact matching
> 2. Add a SQL agent for claims data queries
> 3. Add LangSmith for production observability"

**7. "Why not use LangChain?"**
> "For this scope, LangChain adds complexity without benefit. I wanted to understand every step clearly. For a larger system with multiple chains, LangChain's abstractions would help."

---

# Resume Bullet Points (Lite Version)

**Option 1 - Technical:**
> Built a RAG-based Q&A system for insurance policies using FastAPI, pgvector, and OpenAI, achieving 78% answer accuracy on a 30-question evaluation set with source citations.

**Option 2 - Business Impact:**
> Developed an AI-powered policy research assistant reducing document lookup time, leveraging vector similarity search and LLM generation with verifiable source citations.

**Option 3 - Learning-focused:**
> Designed and implemented a complete RAG pipeline (document ingestion, embedding, retrieval, generation) for insurance domain, demonstrating AI engineering skills through hands-on Python development.

---

# LinkedIn Post

```
🚀 Just shipped my first AI project: PolicyMind Lite

After 1 year in Java/Spring Boot backend development, I wanted to understand how RAG (Retrieval-Augmented Generation) actually works — not just call an API.

So I built a document Q&A system for insurance policies:

📄 Upload policy PDFs
❓ Ask questions in plain English  
✅ Get answers with source citations

Tech stack:
• Python + FastAPI
• PostgreSQL + pgvector
• OpenAI embeddings + GPT-4

Key learnings:
1. Chunking strategy matters more than I expected
2. Vector search alone misses exact matches (policy numbers, codes)
3. Always show sources — users need to verify AI answers

Evaluated on 30 test questions: 78% accuracy

Next steps: Add hybrid search and a SQL agent for claims data.

Code: [GitHub link]

#AIEngineering #RAG #Python #LLM
```

---

# Cost Estimate

| Item | Cost |
|------|------|
| OpenAI Embeddings (50 docs) | ~$0.50 |
| OpenAI GPT-4 (testing) | ~$5-10 |
| OpenAI GPT-4 (evaluation) | ~$3 |
| PostgreSQL (local Docker) | Free |
| **Total** | **~$15** |

---

# Comparison: Lite vs Full

| Aspect | Lite (This Plan) | Full (Previous Plan) |
|--------|-----------------|---------------------|
| Build Time | 2 weeks | 4 weeks |
| Complexity | Simple | Complex |
| Interview Risk | Low | Medium-High |
| Impressiveness | Good for 1 YoE | Great for 2-3 YoE |
| Can Explain Everything? | Yes | Maybe |
| Upgrade Path | Easy to extend | Already maxed |

---

# When to Upgrade to Full Version

Do the full version AFTER you:
1. ✅ Complete the Lite version
2. ✅ Can explain every line of code
3. ✅ Get comfortable with the evaluation metrics
4. ✅ Want to add one feature at a time

**Upgrade path:**
- Week 3: Add hybrid search (BM25 + vector)
- Week 4: Add SQL agent for claims queries
- Week 5: Add LangGraph orchestration
- Week 6: Add RAGAS evaluation + LangSmith

Each addition becomes a new talking point without overwhelming you.

---

*This plan is designed for engineers with ~1 year experience transitioning to AI roles.*
*Every component is explainable. Every decision is defensible.*
