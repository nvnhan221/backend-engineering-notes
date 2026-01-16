# Backend Engineering Notes

📚 Documentation repository following the **Docs-as-code** approach using [MkDocs Material](https://squidfunk.github.io/mkdocs-material/).

## 🚀 Quick Start

### Prerequisites

- Python 3.8 or higher
- pip (Python package manager)
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/nvnhan221/backend-engineering-notes.git
   cd backend-engineering-notes
   ```

2. **Create virtual environment** (recommended)
   ```bash
   python3 -m venv venv
   source venv/bin/activate  # On Windows: venv\Scripts\activate
   ```

3. **Install dependencies**
   ```bash
   pip install -r requirements.txt
   ```

4. **Start development server**
   ```bash
   mkdocs serve
   # or use Makefile
   make serve
   ```

   Documentation will be available at `http://127.0.0.1:8000`

## 📁 Project Structure

```
.
├── docs/                      # Documentation source files (Markdown)
│   ├── index.md              # Home page
│   ├── getting-started/      # Getting started guides
│   ├── guides/               # Comprehensive guides
│   ├── examples/             # Example documentation
│   └── api/                  # API reference
├── examples/                 # Runnable code examples
│   ├── api/                  # API examples
│   └── ...
├── .github/
│   └── workflows/
│       └── deploy.yml        # CI/CD for GitHub Pages
├── mkdocs.yml                # MkDocs configuration
├── requirements.txt          # Python dependencies
├── Makefile                  # Convenience commands
└── README.md                 # This file
```

## 🛠️ Available Commands

Using Makefile:

```bash
make help      # Show all available commands
make install   # Install dependencies
make serve     # Start development server
make build     # Build static site
make deploy    # Deploy to GitHub Pages
make clean     # Clean build artifacts
```

Or use MkDocs directly:

```bash
mkdocs serve          # Development server with live reload
mkdocs build          # Build static site to site/
mkdocs gh-deploy      # Deploy to GitHub Pages
```

## 📝 Writing Documentation

### Adding New Pages

1. Create a new `.md` file in the `docs/` directory
2. Add it to the `nav` section in `mkdocs.yml`
3. Write your content in Markdown
4. The page will appear in the navigation automatically

### Markdown Features

This site supports extended Markdown features:

- **Code highlighting** with syntax highlighting
- **Admonitions** (notes, warnings, tips)
- **Tabs** for code examples
- **Math equations** with MathJax
- **Diagrams** with Mermaid
- **Tables** and more

See the [Quick Start Guide](docs/getting-started/quick-start.md) for examples.

## 🌐 Deployment

### GitHub Pages (Automatic)

The repository includes a GitHub Actions workflow that automatically deploys the documentation to GitHub Pages when you push to the `main` branch.

1. Enable GitHub Pages in repository settings
2. Select source: `gh-pages` branch
3. Push to `main` branch - deployment happens automatically

### Manual Deployment

```bash
mkdocs gh-deploy
```

### Other Static Hosting

After building:

```bash
mkdocs build
```

The static site will be in the `site/` directory, which can be deployed to:

- Netlify
- Vercel
- AWS S3
- Any static hosting service

## 🎨 Customization

### Theme Configuration

Edit `mkdocs.yml` to customize:

- Site name and description
- Theme colors and palette
- Navigation structure
- Plugins and extensions

### Adding Plugins

1. Install the plugin: `pip install mkdocs-plugin-name`
2. Add to `requirements.txt`
3. Configure in `mkdocs.yml` under `plugins:`

## 📚 Documentation Features

- ✅ **Material Design** - Beautiful, responsive UI
- ✅ **Dark Mode** - Easy on the eyes
- ✅ **Full-text Search** - Find content quickly
- ✅ **Mobile Friendly** - Works on all devices
- ✅ **Version Control** - Track changes with Git
- ✅ **Git Revision Dates** - See when pages were last updated
- ✅ **Code Copy** - One-click code copying
- ✅ **Syntax Highlighting** - Beautiful code blocks

## 🤝 Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## 📄 License

This project is open source and available under the MIT License.

## 🔗 Resources

- [MkDocs Documentation](https://www.mkdocs.org/)
- [MkDocs Material](https://squidfunk.github.io/mkdocs-material/)
- [Markdown Guide](https://www.markdownguide.org/)

## 📞 Support

For issues and questions:

- Open an issue on GitHub
- Check the [documentation](docs/index.md)
- Review the [examples](examples/README.md)

---

**Built with ❤️ using MkDocs Material**
