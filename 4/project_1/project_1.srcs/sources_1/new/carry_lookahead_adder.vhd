library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity carry_lookahead_adder is
    Port ( x : in STD_LOGIC_VECTOR (3 downto 0);
           y : in STD_LOGIC_VECTOR (3 downto 0);
           c_in : in STD_LOGIC;
           sum : out std_logic_vector (3 downto 0);
           c_out : out std_logic);
end carry_lookahead_adder;

architecture Behavioral of carry_lookahead_adder is

component carry_block is
    Port ( x : in STD_LOGIC;
           y : in STD_LOGIC;
           c_in : in STD_LOGIC;
           p : out STD_LOGIC;
           g : out STD_LOGIC;
           s : out std_logic;
           c_out : out STD_LOGIC);
end component;

signal tmp : std_logic_vector (4 downto 0) := (others => '0');
signal p,g,s : std_logic_vector(3 downto 0) := (others => '0' );
begin
    tmp(0) <= c_in; -- practic c4...c0
    cb_1 : carry_block port map(x(0), y(0), tmp(0), p(0), g(0), s(0), tmp(1));
    cb_2 : carry_block port map(x(1), y(1), tmp(1), p(1), g(1), s(1), tmp(2));
    cb_3 : carry_block port map(x(2), y(2), tmp(2), p(2), g(2), s(2), tmp(3));
    cb_4 : carry_block port map(x(3), y(3), tmp(3), p(3), g(3), s(3), tmp(4));
    sum <= s;
    c_out <= tmp(4);
    
end Behavioral;
