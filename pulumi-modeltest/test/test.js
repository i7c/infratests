let assert = require("assert");
let mocha = require("mocha");
let pulumi = require("@pulumi/pulumi");
let infra = require("../index");

describe("Infrastructure", function() {
    let appService = infra.appService;

    describe("#service", function() {
        it("must be in westeurope", function(done) {

            pulumi.all([appService.urn, appService.location]).apply(([urn, location]) => {
                if (location != "westeurope") {
                    done(new Error("Wrong locatino for ${urn}"));
                } else {
                    done();
                }

            });
        });
    });
});
