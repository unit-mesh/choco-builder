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

function parseFile(path) {
    const markdownContent = fs.readFileSync(path, 'utf8');

    const parsedData = matter(markdownContent);
    const frontmatter = parsedData.data;
    const components = frontmatter.components || [];

    const tokens = marked.lexer(markdownContent);

    let title = '';
    let componentName = '';
    let description = '';

    for (const token of tokens) {
        if (token.type === 'heading' && token.depth === 1) {
            // Extract the component name from the top-level heading
            componentName = token.text;
        } else if (token.type === 'heading' && token.depth === 2) {
            // Extract the title from the second-level heading
            title = token.text;
        } else if (token.type === 'paragraph') {
            // Extract the description from the paragraph with class "description"
            const match = token.text.match(/<p class="description">(.*?)<\/p>/);
            if (match) {
                description = match[1];
            }
        }
    }

    console.log(components);
    console.log(title);
    console.log(description);
}

parseFile('/Users/phodal/test/material-ui/docs/data/joy/components/chip/chip-zh.md')
