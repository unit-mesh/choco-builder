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

function parseFile(path) {
    const markdownContent = fs.readFileSync(path, 'utf8');

    const parsedData = matter(markdownContent);
    const frontmatter = parsedData.data;
    const components = frontmatter.components || [];

    const tokens = marked.lexer(markdownContent);

    let title = '';
    let description = '';

    for (const token of tokens) {
        if (token.type === 'heading' && token.depth === 1) {
            title = token.text;
        } else if (token.type === 'html') {
            const dom = new JSDOM(parsedData.content);
            const descriptionElement = dom.window.document.querySelector('p.description');
            description = descriptionElement ? descriptionElement.textContent : '';
        } else {
            // console.log(token.type)
        }
    }

    console.log(components);
    console.log(title);
    console.log(description);
}

parseFile('/Users/phodal/test/material-ui/docs/data/joy/components/chip/chip-zh.md')
