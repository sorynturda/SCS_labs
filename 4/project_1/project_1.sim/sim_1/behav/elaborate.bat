@echo off
set xv_path=C:\\Xilinx\\Vivado\\2016.4\\bin
call %xv_path%/xelab  -wto ec347957f9da4a1ca098221cf3f77a52 -m64 --debug typical --relax --mt 2 -L xil_defaultlib -L secureip --snapshot inmultire_tb_behav xil_defaultlib.inmultire_tb -log elaborate.log
if "%errorlevel%"=="0" goto SUCCESS
if "%errorlevel%"=="1" goto END
:END
exit 1
:SUCCESS
exit 0
