library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity shift_reg8 is
    Port ( clk : in STD_LOGIC;
           en : in STD_LOGIC;
           si : in STD_LOGIC;
           so : out STD_LOGIC);
end shift_reg8;

architecture Behavioral of shift_reg8 is

signal tmp : std_logic_vector(7 downto 0);

begin
    process(clk)
    begin
        if(rising_edge(clk)) then
            if en = '1' then
                for i in 0 to 6 loop
                    tmp(i + 1) <= tmp(i);
                end loop;
                tmp(0) <= si;
            end if;
        end if;
    end process;
    so <= tmp(7);
end Behavioral;
