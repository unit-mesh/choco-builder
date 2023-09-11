// walk dir in /Users/phodal/test/material-ui/docs/data to get files end with `.zh.md` and `.ts.preview`
const fs = require('fs');
const path = require('path');
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
            if (file.endsWith('-zh.md') || file.endsWith('.tsx.preview')) {
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

const dirPath = path.join('/Users/phodal/test/material-ui', 'docs/data/');
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
        if (item.endsWith('-zh.md')) {
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
        if (item.endsWith('-zh.md')) {
            let results = parseMd(item);
            componentInfo.title = results.title;
            componentInfo.description = results.description;
            componentInfo.components = results.components;
        }
        if (item.endsWith('.tsx.preview')) {
            var name = item.split('/').pop()
            name = name.replace('.tsx.preview', '');
            componentInfo.examples.push({
                name: name,
                content: fs.readFileSync(item, 'utf8')
            });
        }
    });

    return componentInfo;
});

// console.log(components);
// write to file
fs.writeFileSync('./mui-parser.json', JSON.stringify(components, null, 2));