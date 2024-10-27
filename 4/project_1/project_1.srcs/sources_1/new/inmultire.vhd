--wallace tree multiplier
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity inmultire is
    Port ( x : in STD_LOGIC_VECTOR (3 downto 0);
           y : in STD_LOGIC_VECTOR (3 downto 0);
           i : out STD_LOGIC_VECTOR (7 downto 0));
end inmultire;

architecture Behavioral of inmultire is

component carry_block is
    Port ( x : in STD_LOGIC;
           y : in STD_LOGIC;
           c_in : in STD_LOGIC;
           p : out STD_LOGIC;
           g : out STD_LOGIC;
           s : out std_logic;
           c_out : out STD_LOGIC);
end component;

signal  p, c : std_logic_vector (11 downto 0) := (others => '0');
signal tmp : std_logic_vector (15 downto 0) := (others => '0');

begin
    tmp(0) <= x(2) and y(1);                            
    tmp(1) <= x(3) and y(0);
    tmp(2) <= x(3) and y(1);
    tmp(3) <= x(2) and y(2);
    tmp(4) <= x(2) and y(0);
    tmp(5) <= x(1) and y(1);
    tmp(6) <= x(0) and y(3);
    tmp(7) <= x(1) and y(3);
    tmp(8) <= x(2) and y(3);
    tmp(9) <= x(3) and y(2);
    tmp(10) <= x(0) and y(0);  --concat
    tmp(11) <= x(0) and y(1);
    tmp(12) <= x(1) and y(0);
    tmp(13) <= x(0) and y(2);
    tmp(14) <= x(3) and y(3);
    tmp(15) <= x(1) and y(2);



    fa_1 : carry_block port map(
        x => tmp(0), 
        y => tmp(1), 
        c_in => '0', 
        p => p(0), 
        g => open, 
        s => open, 
        c_out => c(0));
    fa_2 : carry_block port map(tmp(2), tmp(3), '0', p(1), open, open, c(1));
    fa_3 : carry_block port map(tmp(4), tmp(5), '0', p(2), open, open, c(2));
    fa_4 : carry_block port map(tmp(6), p(0), tmp(15), p(3), open, open, c(3));
    fa_5 : carry_block port map(tmp(7), p(1), c(0), p(4), open, open, c(4));
    fa_6 : carry_block port map(tmp(8), tmp(9), c(1), p(5), open, open, c(5));
    fa_7 : carry_block port map(tmp(11), tmp(12), '0', p(6), open, open, c(6));
    fa_8 : carry_block port map(tmp(13), p(2), c(6), p(7), open, open, c(7));
    fa_9 : carry_block port map(c(2), p(3), c(7), p(8), open, open, c(8));
    fa_10 : carry_block port map(c(3), p(4), c(8), p(9), open, open, c(9));
    fa_11 : carry_block port map(c(4), p(5), c(9), p(10), open, open, c(10));
    fa_12 : carry_block port map(tmp(14), c(5), c(10), p(11), open, open, c(11));

i <= c(11) & p(11) & p(10) & p(9) & p(8) & p(7) & p(6) & tmp(10);



end Behavioral;
