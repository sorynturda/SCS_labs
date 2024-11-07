-- Copyright 1986-2016 Xilinx, Inc. All Rights Reserved.
-- --------------------------------------------------------------------------------
-- Tool Version: Vivado v.2016.4 (win64) Build 1756540 Mon Jan 23 19:11:23 MST 2017
-- Date        : Thu Nov 07 11:37:32 2024
-- Host        : asus-sorin running 64-bit major release  (build 9200)
-- Command     : write_vhdl -force -mode funcsim {c:/Users/Sorin
--               Turda/Desktop/SCS_labs/6/project_1/project_1.srcs/sources_1/ip/fp_absolute_value/fp_absolute_value_sim_netlist.vhdl}
-- Design      : fp_absolute_value
-- Purpose     : This VHDL netlist is a functional simulation representation of the design and should not be modified or
--               synthesized. This netlist cannot be used for SDF annotated simulation.
-- Device      : xc7a35tcpg236-1
-- --------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
library UNISIM;
use UNISIM.VCOMPONENTS.ALL;
entity fp_absolute_value is
  port (
    s_axis_a_tvalid : in STD_LOGIC;
    s_axis_a_tready : out STD_LOGIC;
    s_axis_a_tdata : in STD_LOGIC_VECTOR ( 31 downto 0 );
    m_axis_result_tvalid : out STD_LOGIC;
    m_axis_result_tready : in STD_LOGIC;
    m_axis_result_tdata : out STD_LOGIC_VECTOR ( 31 downto 0 )
  );
  attribute NotValidForBitStream : boolean;
  attribute NotValidForBitStream of fp_absolute_value : entity is true;
  attribute CHECK_LICENSE_TYPE : string;
  attribute CHECK_LICENSE_TYPE of fp_absolute_value : entity is "fp_absolute_value,floating_point_v7_1_3,{}";
  attribute downgradeipidentifiedwarnings : string;
  attribute downgradeipidentifiedwarnings of fp_absolute_value : entity is "yes";
  attribute x_core_info : string;
  attribute x_core_info of fp_absolute_value : entity is "floating_point_v7_1_3,Vivado 2016.4";
end fp_absolute_value;

architecture STRUCTURE of fp_absolute_value is
  signal \<const0>\ : STD_LOGIC;
  signal \^m_axis_result_tready\ : STD_LOGIC;
  signal \^s_axis_a_tdata\ : STD_LOGIC_VECTOR ( 31 downto 0 );
  signal \^s_axis_a_tvalid\ : STD_LOGIC;
begin
  \^m_axis_result_tready\ <= m_axis_result_tready;
  \^s_axis_a_tdata\(30 downto 0) <= s_axis_a_tdata(30 downto 0);
  \^s_axis_a_tvalid\ <= s_axis_a_tvalid;
  m_axis_result_tdata(31) <= \<const0>\;
  m_axis_result_tdata(30 downto 0) <= \^s_axis_a_tdata\(30 downto 0);
  m_axis_result_tvalid <= \^s_axis_a_tvalid\;
  s_axis_a_tready <= \^m_axis_result_tready\;
GND: unisim.vcomponents.GND
     port map (
      G => \<const0>\
    );
end STRUCTURE;
