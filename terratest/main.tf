resource "azurerm_resource_group" "example" {
  name     = "example-terratest"
  location = "westeurope"
}

resource "azurerm_app_service_plan" "example" {
  name                = "example-terratest"
  location            = azurerm_resource_group.example.location
  resource_group_name = azurerm_resource_group.example.name

  kind = "Linux"

  sku {
    tier = "Basic"
    size = "B1"
  }

  reserved = true
}

resource "azurerm_app_service" "example" {
  name                = "example-terratest-constantin"
  location            = azurerm_resource_group.example.location
  resource_group_name = azurerm_resource_group.example.name
  app_service_plan_id = azurerm_app_service_plan.example.id

  site_config {
    linux_fx_version = "DOCKER|strm/helloworld-http:latest"
    always_on        = true
  }

  app_settings = {
    "WEBSITES_ENABLE_APP_SERVICE_STORAGE" = false
  }

  identity {
    type = "SystemAssigned"
  }
}

output "default_site_hostname" {
  value = azurerm_app_service.example.default_site_hostname
}
