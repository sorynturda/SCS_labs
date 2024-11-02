library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use ieee.std_logic_unsigned.all;
use ieee.numeric_std.all;


entity fifo_8bit is
    Port ( rd : in STD_LOGIC;
           data_in : in STD_LOGIC_VECTOR (7 downto 0);
           wr : in STD_LOGIC;
           wr_inc : in STD_LOGIC;
           rd_inc : in STD_LOGIC;
           rst : in STD_LOGIC;
           clk : in STD_LOGIC;
           data_out : out STD_LOGIC_VECTOR (7 downto 0));
end fifo_8bit;

architecture Behavioral of fifo_8bit is

signal wr_ptr, rd_ptr : std_logic_vector(2 downto 0) := (others => '0');
signal fifo_select : std_logic_vector (7 downto 0) := (others => '0');

type reg_array is array(0 to 7) of std_logic_vector(7 downto 0);
signal reg : reg_array := (others => "00000000");

signal index : std_logic := '0';

begin

    write_pointer : process (clk)
        begin
        if rst = '1' then
            wr_ptr <= "000";
        else 
            if rising_edge(clk) and wr_inc = '1' then
                wr_ptr <= wr_ptr + 1;
            end if;
        end if;
    end process write_pointer;

    read_pointer : process (clk)
    begin
        if rst = '1' then
            rd_ptr <= "000";
        else 
            if rising_edge(clk) and rd_inc = '1' then
                rd_ptr <= rd_ptr + 1;
            end if;
        end if;
    end process read_pointer;
    
    fifo_select <=  "00000001" when wr_ptr = "000" else
                    "00000010" when wr_ptr = "001" else
                    "00000100" when wr_ptr = "010" else
                    "00001000" when wr_ptr = "011" else
                    "00010000" when wr_ptr = "100" else
                    "00100000" when wr_ptr = "101" else
                    "01000000" when wr_ptr = "110" else
                    "10000000";   
    
    write_in_fifo : process(clk, wr, data_in)
    begin
        if rst = '1' then
            for i in 0 to 7 loop
                reg(i) <= (others => '0');
            end loop;
        else
            if rising_edge(clk) and wr = '1' then
                for i in 0 to 7 loop
                    if fifo_select(i) = '1' then
                        reg(i) <= data_in;
                    end if; 
                end loop;
            end if;
        end if;
    end process;   
    
    mux : process(rd, rd_ptr, reg)
    begin
        if rd = '1' then
            case rd_ptr is
                when "000" => data_out <= reg(0);
                when "001" => data_out <= reg(1);
                when "010" => data_out <= reg(2);
                when "011" => data_out <= reg(3);
                when "100" => data_out <= reg(4);
                when "101" => data_out <= reg(5);
                when "110" => data_out <= reg(6);
                when "111" => data_out <= reg(7);
                when others => data_out <= X"FF";
            end case;
         end if;
    end process mux;
    

end Behavioral;
