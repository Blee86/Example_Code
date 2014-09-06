#!/usr/bin/env python
#
# Copyright 2014 Virginia Polytechnic Institute and State University
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""
ECE 4564
Assignment 0

This module provides a function for calculating a CRC7
"""

__author__ = "Thaddeus Czauski"

import BitVector

def generate_crc(data):
    """Generate the CRC7 for the given data

    Generates a CRC7 checksum for the given data as specified in [1]_. The CRC
    generation algorithm is based on the simple polynomial division code
    fragment from Wikipedia [2]_.

    The data to checksum must be loaded into a BitVector of the appropriate size
    of the message. Furthermore, the BitVector should al least be 7 bits long to
    calculate a CRC7.

    .. [1] SD Group and SD Card Association,
    "Cyclic Redundancy Code (CRC)" in
    *Physical Layer Simplified Specification*,
    Version 4.10, Jan. 2013, sec. 4.5.
    [Online]. Available: https://www.sdcard.org/downloads/pls/

    .. [2] Wikipedia, (2014, Aug 30) *Computation of cyclic redundancy checks
    - Wikipedia, The Free Encyclopedia* [Online]. Available:
    https://en.wikipedia.org/w/index.php?title=Computation_of_cyclic_redundancy_checks&oldid=621392354

    :param data: (BitVector.BitVector) The data to be checksummed
    :return: (BitVector.BitVector) The CRC7 value for the given data
    :raises ValueError: if data is too short to checksum
    :raises TypeError: if data is not a BitVector
    """
    if isinstance(data, BitVector.BitVector):
        # G(x) = x**7 + x**3 + 1
        generator = BitVector.BitVector(intVal = 0x09, size = 7)

        try:
            remainder = data[:generator.size]
        except ValueError:
            raise ValueError("Data is too short to checksum")

        for i in xrange(data.size):
                check_bit_n = remainder[0]
                remainder.shift_left(1)

                try:
                    remainder[remainder.size - 1] = data[i + remainder.size]

                except ValueError:
                    # Reached the end of data,
                    # Use the zero that was shifted in from BitVector.shift_left
                    pass

                finally:
                    if check_bit_n:
                        remainder ^= generator

        return remainder

    else:
        raise TypeError("Data to checksum must be in BitVector form: " +
                        "found type: " + str(type(data)))
