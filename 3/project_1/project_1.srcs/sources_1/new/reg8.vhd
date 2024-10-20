library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity reg8 is
    Port ( clk : in STD_LOGIC;
           en : in STD_LOGIC;
           d : in STD_LOGIC_VECTOR (7 downto 0);
           q : out STD_LOGIC_VECTOR (7 downto 0));
end reg8;

architecture Behavioral of reg8 is

begin
    process(clk)
    begin
        if (clk = '1' and clk'event) then
            if (en = '1') then
                q <= d;
            end if;
        end if;
    end process;
end Behavioral;
