library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity mux is
    Port ( a : in STD_LOGIC_VECTOR (3 downto 0);
           b : in STD_LOGIC_VECTOR (3 downto 0);
           c : in STD_LOGIC_VECTOR (3 downto 0);
           d : in STD_LOGIC_VECTOR (3 downto 0);
           sel : in STD_LOGIC_VECTOR (1 downto 0);
           x : out STD_LOGIC_VECTOR (3 downto 0));
end mux;

architecture Behavioral of mux is

begin
    with sel select
    x <= a when "00",
         b when "01",
         c when "10",
         d when "11",
         d when others;
end Behavioral;
