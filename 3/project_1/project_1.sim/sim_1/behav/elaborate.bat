@echo off
set xv_path=C:\\Xilinx\\Vivado\\2016.4\\bin
call %xv_path%/xelab  -wto 8624f174b0d3434e8b998af7a37fef50 -m64 --debug typical --relax --mt 2 -L xil_defaultlib -L secureip --snapshot bit8_mux_4_1_tb_behav xil_defaultlib.bit8_mux_4_1_tb -log elaborate.log
if "%errorlevel%"=="0" goto SUCCESS
if "%errorlevel%"=="1" goto END
:END
exit 1
:SUCCESS
exit 0
