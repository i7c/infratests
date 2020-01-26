package test

import (
	"fmt"
	"io/ioutil"
	"net/http"
	"testing"

	"github.com/gruntwork-io/terratest/modules/terraform"
	"github.com/stretchr/testify/assert"
)

func TestTerraformBasicExample(t *testing.T) {
	t.Parallel()

	message := "the answer is 42"

	terraformOptions := &terraform.Options{
		// The path to where our Terraform code is located
		TerraformDir: "..",

		// Variables to pass to our Terraform code using -var options
		Vars: map[string]interface{}{
			"value": message,
		},

		// Disable colors in Terraform commands so its easier to parse stdout/stderr
		NoColor: true,
	}

	// At the end of the test, run `terraform destroy` to clean up any resources that were created
	defer terraform.Destroy(t, terraformOptions)

	// This will run `terraform init` and `terraform apply` and fail the test if there are any errors
	terraform.InitAndApply(t, terraformOptions)

	ipAddress := terraform.Output(t, terraformOptions, "ip_address")

	fmt.Println(ipAddress)

	resp, err := http.Get(fmt.Sprintf("http://%s/file.txt", ipAddress))
	if err != nil {
		assert.Fail(t, "HTTP Request failed")
	}
	defer resp.Body.Close()
	body, err := ioutil.ReadAll(resp.Body)

	assert.Equal(t, message, string(body))
}
