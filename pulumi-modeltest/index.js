"use strict";
const pulumi = require("@pulumi/pulumi");
const azure = require("@pulumi/azure");

const resourceGroup = new azure.core.ResourceGroup("pulumi-example", {
    location: "westeurope",
});
const servicePlan = new azure.appservice.Plan("pulumi-example", {
    resourceGroupName: resourceGroup.name,
    location: resourceGroup.location,
    kind: "Linux",
    reserved: true,
    sku: {
        tier: "Basic",
        size: "B1"
    }
});

exports.appService = new azure.appservice.AppService("pulumi-example", {
    resourceGroupName: resourceGroup.name,
    location: resourceGroup.location,
    appServicePlanId: servicePlan.id,
    siteConfig: {
        alwaysOn: true,
        linuxFxVersion: "DOCKER|strm/helloworld-http:latest"
    },
    identity: {
        type: "SystemAssigned"
    }
});
