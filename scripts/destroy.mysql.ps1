#!\usr\bin\env pwsh

$AppName=cf-archivist
$RegistryName=hooverRegistry

$AppId = (cf app $AppName --guid) | Out-String

if ($AppId) {
	cf stop $AppName
	cf unbind-service $AppName $AppName-secrets
	cf delete-service $AppName-secrets -f
	cf unbind-service $AppName $AppName-backend
	cf delete-service $AppName-backend -f
	cf unbind-service $AppName $RegistryName
	cf delete $AppName -r -f
else {
    Write-Host "$AppName does not exist"
}
