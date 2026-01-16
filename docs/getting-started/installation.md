# Installation

This guide will help you set up the documentation environment locally.

## Prerequisites

- Python 3.8 or higher
- pip (Python package manager)
- Git

## Step 1: Clone the Repository

```bash
git clone https://github.com/nvnhan221/backend-engineering-notes.git
cd backend-engineering-notes
```

## Step 2: Create Virtual Environment

It's recommended to use a virtual environment to isolate dependencies:

```bash
python3 -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
```

## Step 3: Install Dependencies

```bash
pip install -r requirements.txt
```

## Step 4: Verify Installation

Check that MkDocs is installed correctly:

```bash
mkdocs --version
```

## Step 5: Serve Locally

Start the development server:

```bash
mkdocs serve
```

The documentation will be available at `http://127.0.0.1:8000`

## Next Steps

- Read the [Quick Start Guide](quick-start.md) to learn how to write documentation
- Check out the [Examples](../examples/index.md) for code samples
- Explore the [Guides](../guides/index.md) for detailed tutorials
