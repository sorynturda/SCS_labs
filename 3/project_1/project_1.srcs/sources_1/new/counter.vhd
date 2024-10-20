library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use ieee.std_logic_unsigned.all;

entity counter is
    Port ( clk : in STD_LOGIC;
           count : out STD_LOGIC_VECTOR (2 downto 0));
end counter;

architecture Behavioral of counter is

signal tmp : STD_LOGIC_VECTOR (2 downto 0) := (others => '0');

begin
    cnt : process(clk)
    begin
        if rising_edge(clk) then
            tmp <= tmp + 1;
        end if;
    end process cnt;
    
    count <= tmp;
end Behavioral;
