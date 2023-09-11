// walk dir in /Users/phodal/test/material-ui/docs/data to get files end with `.zh.md` and `.ts.preview`
const fs = require('fs');
const path = require('path');
const acorn = require("acorn");
const jsx = require("acorn-jsx");
let jsxParser = acorn.Parser.extend(jsx());

const parseMd = require("./docs-parser");

const needToHandleDir = {}

function walkDir(dir) {
    const files = fs.readdirSync(dir);

    files.forEach(file => {
        const filePath = path.join(dir, file);
        const stat = fs.statSync(filePath);

        if (stat.isDirectory()) {
            walkDir(filePath);
        } else {
            const dirName = path.basename(path.dirname(filePath));
            if (file.endsWith(dirName + '.md') || file.endsWith('.tsx.preview')) {
                const component = path.dirname(filePath);
                if (needToHandleDir[component]) {
                    needToHandleDir[component].push(filePath);
                } else {
                    needToHandleDir[component] = [filePath];
                }
            }
        }
    });
}

const dirPath = path.join('/Users/phodal/test/material-ui', 'docs/data/base/components');
walkDir(dirPath);
// needToHandleDir will be:
// {
//   '/Users/phodal/test/material-ui/docs/data/base/components/autocomplete/AutocompleteIntroduction/css': [
//     '/Users/phodal/test/material-ui/docs/data/base/components/autocomplete/AutocompleteIntroduction/css/index.tsx.preview'
//   ],
//   '/Users/phodal/test/material-ui/docs/data/base/components/autocomplete/AutocompleteIntroduction/system': [
//     '/Users/phodal/test/material-ui/docs/data/base/components/autocomplete/AutocompleteIntroduction/system/index.tsx.preview'
//   ],
// }
//  we need to exists -zh.md and .tsx.preview
const result = Object.keys(needToHandleDir).filter(key => {
    let items = needToHandleDir[key];
    let hasZhMd = false;
    let hasTsxPreview = false;

    items.forEach(item => {
        const dirName = path.basename(path.dirname(item));
        if (item.endsWith(dirName + '.md')) {
            hasZhMd = true;
        } else if (item.endsWith('.tsx.preview')) {
            hasTsxPreview = true;
        }
    });

    return hasZhMd && hasTsxPreview;
});

// console.log(result);
const components = result.map(key => {
    const items = needToHandleDir[key];
    const componentInfo = {
        name: key.split('/').pop(),
        title: '',
        description: '',
        components: [],
        examples: [],
    };

    items.forEach(item => {
        if (item.endsWith('.md')) {
            let results = parseMd(item);
            componentInfo.title = results.title;
            componentInfo.description = results.description;
            componentInfo.components = results.components;
        }
        if (item.endsWith('.tsx.preview')) {
            let name = item.split('/').pop();
            name = name.replace('.tsx.preview', '');
            componentInfo.examples.push({
                name: name,
                content: fs.readFileSync(item, 'utf8')
            });
        }
    });

    if (componentInfo.components.length === 0 && componentInfo.examples.length > 0) {
        var maybeTagName = componentInfo.title
        // if title same with example html tag name, then use it; like title is `Accordion`, example is <Accordion /..>
        componentInfo.examples.forEach(example => {
            if (example.content.startsWith(`<${maybeTagName}`)) {
                componentInfo.components = [maybeTagName]
            }
        });
    }

    return componentInfo;
});

// console.log(components);
// write to file
fs.writeFileSync('./mui-components.json', JSON.stringify(components, null, 2));