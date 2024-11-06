library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use ieee.std_logic_unsigned.all;

entity fifo_ctrl is
    Port ( rd : in STD_LOGIC;
           wr : in STD_LOGIC;
           clk : in STD_LOGIC;
           rst : in STD_LOGIC;
           rd_inc : out STD_LOGIC;
           wr_inc : out STD_LOGIC;
           full : out STD_LOGIC;
           empty : out STD_LOGIC);
end fifo_ctrl;

architecture Behavioral of fifo_ctrl is

signal rd_tmp, wr_tmp : std_logic_vector (2 downto 0) := (others => '0');

begin
    read : process (clk, rd, rst)
    begin
        if rst = '1' then
            rd_tmp <= "000";
        else
            if rising_edge(clk) then
                if rd = '1' then
                    rd_tmp <= rd_tmp + 1;
                end if;
                rd_inc <= rd;
            end if; 
        end if;
    end process;

    write : process (clk, wr, rst)
    begin
        if rst = '1' then
            wr_tmp <= "000";
        else
            if rising_edge(clk) then
                if wr = '1' then
                    wr_tmp <= wr_tmp + 1;
                end if;
                wr_inc <= wr;
            end if; 
        end if;
    end process;

    full_empty : process(wr_tmp, rd_tmp)
    begin
        if wr_tmp = "000" or rd_tmp = "111" then
            empty <= '1';
        else
            empty <= '0';
        end if;
        if wr_tmp = "111" and rd_tmp = "000" then
            full <= '1';
        else
            full <= '0';
        end if;
    end process;
end Behavioral;
