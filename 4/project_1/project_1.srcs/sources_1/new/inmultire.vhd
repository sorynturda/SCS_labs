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

signal tmp, p, c : std_logic_vector (11 downto 0) := (others => '0');
signal zero : std_logic := '0';
begin

fa1 : carry_block port map((x(2) and y(1)), (x(3) and y(0)), zero, p(0), open, open, c(0));

end Behavioral;
