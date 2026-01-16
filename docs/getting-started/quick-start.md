# Quick Start

Learn how to write and build documentation with MkDocs Material.

## Writing Documentation

All documentation is written in Markdown and stored in the `docs/` directory.

### Basic Markdown

```markdown
# Heading 1
## Heading 2
### Heading 3

**Bold text** and *italic text*

- List item 1
- List item 2

1. Numbered item 1
2. Numbered item 2
```

### Code Blocks

````markdown
```python
def hello_world():
    print("Hello, World!")
```
````

### Admonitions

!!! note "Note"
    This is a note admonition.

!!! warning "Warning"
    This is a warning.

!!! tip "Tip"
    This is a helpful tip.

### Tabs

=== "Python"
    ```python
    print("Hello from Python")
    ```

=== "JavaScript"
    ```javascript
    console.log("Hello from JavaScript");
    ```

## Building the Site

### Development Server

```bash
mkdocs serve
```

This starts a local development server with live reload.

### Build Static Site

```bash
mkdocs build
```

This generates a static site in the `site/` directory.

### Deploy

```bash
mkdocs gh-deploy
```

This deploys the site to GitHub Pages.

## Project Structure

```
.
├── docs/              # Documentation source files
│   ├── index.md       # Home page
│   └── ...
├── examples/          # Code examples
├── mkdocs.yml         # MkDocs configuration
└── requirements.txt   # Python dependencies
```

## Adding New Pages

1. Create a new `.md` file in the `docs/` directory
2. Add it to the `nav` section in `mkdocs.yml`
3. Write your content in Markdown
4. The page will appear in the navigation automatically

## Next Steps

- Explore the [Examples](../examples/index.md) for more advanced features
- Read the [Guides](../guides/index.md) for detailed tutorials
- Check the [MkDocs Material documentation](https://squidfunk.github.io/mkdocs-material/) for more features
