// docs examples:
// ```markdown
// ---
// productId: joy-ui
// title: React Chip component
// components: Chip, ChipDelete
// githubLabel: 'component: chip'
// ---
//
// # Chip
//
// <p class="description">Chip generates a compact element that can represent an input, attribute, or action.</p>
//
// ## Introduction
//
// ```
// we need to get the components name, title and description from the markdown file
const marked = require('marked');
const matter = require('gray-matter');
const fs = require('fs');
const {JSDOM} = require('jsdom');

function parseMd(path) {
    const markdownContent = fs.readFileSync(path, 'utf8');

    const parsedData = matter(markdownContent);
    const frontmatter = parsedData.data;
    let components = [];
    if (frontmatter.components) {
        components = frontmatter.components.split(',').map(item => item.trim());
    }

    const tokens = marked.lexer(markdownContent);
    const examples = [];

    let title = '';
    let description = '';

    let isStartComponent = true;
    let componentSectionName = '';

    for (const token of tokens) {
        if (token.type === 'heading' && token.depth === 1) {
            title = token.text;
        } else if (token.type === 'html') {
            try {
                const dom = new JSDOM(token.text);
                const descriptionElement = dom.window.document.querySelector('p.description');
                description = descriptionElement ? descriptionElement.textContent : '';
            } catch (e) {
                console.log(e);
            }
        } else if (token.type === 'heading' && token.depth === 2) {
            if (token.text.trim() === 'Component') {
                isStartComponent = true;
            }
        } else if (isStartComponent && token.type === 'heading' && token.depth === 3) {
            componentSectionName = token.text;
        } else if (token.type === 'paragraph') {
            // console.log(token.text)
            // console.log("\n")
        }
    }

    return {
        title,
        components,
        description
    }
}

module.exports = parseMd;
