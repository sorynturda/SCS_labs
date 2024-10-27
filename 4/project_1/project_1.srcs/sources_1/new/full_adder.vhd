library IEEE;
use IEEE.STD_LOGIC_1164.ALL;


entity full_adder is
    Port ( x : in STD_LOGIC;
           y : in STD_LOGIC;
           c_in : in STD_LOGIC;
           s : out STD_LOGIC;
           c_out : out STD_LOGIC);
end full_adder;

architecture Behavioral of full_adder is

signal tmp1, tmp2 : std_logic := '0';

begin

tmp1 <= x xor y;
s <= tmp1 xor c_in;
c_out <= (x and y) or (tmp2 and c_in); 

end Behavioral;