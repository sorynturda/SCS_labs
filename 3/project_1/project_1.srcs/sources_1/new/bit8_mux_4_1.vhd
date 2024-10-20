library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity bit8_mux_4_1 is
    Port ( a : out STD_LOGIC_VECTOR (7 downto 0);
           b : out STD_LOGIC_VECTOR (7 downto 0);
           c : out STD_LOGIC_VECTOR (7 downto 0);
           d : out STD_LOGIC_VECTOR (7 downto 0);
           sel : in STD_LOGIC_VECTOR (1 downto 0);
           x : in STD_LOGIC_VECTOR (7 downto 0));
end bit8_mux_4_1;

architecture Behavioral of bit8_mux_4_1 is

begin
    process(x, sel)
    begin
        a <= (others => '0');
        b <= (others => '0');
        c <= (others => '0');
        d <= (others => '0');
        case sel is
            when "00" => a <= x;
            when "01" => b <= x;
            when "10" => c <= x;
            when "11" => d <= x;
            when others => null;
        end case;
    end process;

end Behavioral;
