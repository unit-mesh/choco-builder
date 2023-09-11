// walk dir in /Users/phodal/test/material-ui/docs/data to get files end with `.zh.md` and `.ts.preview`
const fs = require('fs');
const path = require('path');

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
                // console.log(filePath);
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
var result = Object.keys(needToHandleDir).filter(key => {
    let items = needToHandleDir[key];
    var hasZhMd = false;
    var hasTsxPreview = false;

    items.forEach(item => {
        if (item.endsWith('-zh.md')) {
            hasZhMd = true;
        } else if (item.endsWith('.tsx.preview')) {
            hasTsxPreview = true;
        }
    });

    return hasZhMd && hasTsxPreview;
});

console.log(result);

