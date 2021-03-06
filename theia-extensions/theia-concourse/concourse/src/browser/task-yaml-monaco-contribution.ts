/// <reference types='monaco-editor-core/monaco'/>
import {
    CONCOURSE_TASK_YAML_LANGUAGE_ID,
    CONCOURSE_TASK_YAML_LANGUAGE_NAME
} from '../common';

// Task .yml file language registration

let YAML_LANG_MODULE_PROMISE: monaco.Promise<any>;

monaco.languages.register({
    id: CONCOURSE_TASK_YAML_LANGUAGE_ID,
    filenamePatterns: ['*task.yml', '**/tasks/*.yml'],
    aliases: [CONCOURSE_TASK_YAML_LANGUAGE_NAME, CONCOURSE_TASK_YAML_LANGUAGE_ID],
    firstLine: '^#(\\\\s)*task(\\\\s)*',
});

monaco.languages.onLanguage(CONCOURSE_TASK_YAML_LANGUAGE_ID, () => {
    if (!YAML_LANG_MODULE_PROMISE) {
        YAML_LANG_MODULE_PROMISE = (<any>monaco.languages.getLanguages().find(ext => ext.id === 'yaml')).loader();
    }
    return YAML_LANG_MODULE_PROMISE.then(mod => {
        monaco.languages.setLanguageConfiguration(CONCOURSE_TASK_YAML_LANGUAGE_ID, mod.conf);
        monaco.languages.setMonarchTokensProvider(CONCOURSE_TASK_YAML_LANGUAGE_ID, mod.language);
    })
});
