library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity xor_dataflow is
    Port ( a : in STD_LOGIC;
           b : in STD_LOGIC;
           c : out STD_LOGIC);
end xor_dataflow;

architecture Dataflow of xor_dataflow is

begin   
    c <= a xor b;
end Dataflow;
