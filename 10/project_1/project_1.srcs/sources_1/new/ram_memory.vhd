library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.numeric_std.ALL;


entity ram_memory is
    Port (
        addr : in std_logic_vector(15 downto 0);
        cs : in std_logic; 
        we : in std_logic;
        d : inout std_logic_vector(7 downto 0)
    );
end ram_memory;

architecture Behavioral of ram_memory is

type ram_type is array(0 to 65535) of std_logic_vector (7 downto 0);
signal ram : ram_type := (others => X"00");

begin
    
    d <= ram (to_integer(unsigned(addr))) when cs = '0' else "ZZZZZZZZ";
    ram(to_integer(unsigned(addr))) <= d when we = '0' and cs = '0';

end Behavioral;
