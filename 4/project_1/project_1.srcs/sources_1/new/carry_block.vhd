library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity carry_block is
    Port ( x : in STD_LOGIC;
           y : in STD_LOGIC;
           c_in : in STD_LOGIC;
           p : out STD_LOGIC;
           g : out STD_LOGIC;
           s : out std_logic;
           c_out : out STD_LOGIC);
end carry_block;

architecture Behavioral of carry_block is

signal tmp1, tmp2 : std_logic := '0';

begin

    tmp1 <= x or y;
    tmp2 <= x and y;
    c_out <= tmp2 or (tmp2 and c_in);
    g <= tmp2;
    p <= tmp1;
    s <= x xor y xor c_in;
end Behavioral;
