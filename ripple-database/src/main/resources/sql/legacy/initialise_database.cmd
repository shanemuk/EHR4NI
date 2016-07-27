@echo off
for /F "tokens=*" %%A in (sql_script_run_order.info) do (
	mysql -u root < %%A
)
