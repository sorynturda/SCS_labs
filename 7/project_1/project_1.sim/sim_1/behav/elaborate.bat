@echo off
set xv_path=C:\\Xilinx\\Vivado\\2016.4\\bin
call %xv_path%/xelab  -wto 8aa097bc93934a02a42035563558af1a -m64 --debug typical --relax --mt 2 -L xil_defaultlib -L secureip --snapshot saturator_tb_behav xil_defaultlib.saturator_tb -log elaborate.log
if "%errorlevel%"=="0" goto SUCCESS
if "%errorlevel%"=="1" goto END
:END
exit 1
:SUCCESS
exit 0
