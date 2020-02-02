PULUMI_TEST_MODE=true  \
PULUMI_NODEJS_STACK="my-ws" \
PULUMI_NODEJS_PROJECT="dev" \
PULUMI_CONFIG='{ "azure:region": "westeurope" }'  \
node_modules/mocha/bin/mocha
