/*
 *
 *  * Copyright 2015 Skymind,Inc.
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package org.deeplearning4j.datasets.fetchers;

import java.util.List;

import org.deeplearning4j.datasets.iterator.DataSetFetcher;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.util.FeatureUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A base class for assisting with creation of matrices
 * with the data applyTransformToDestination fetcher
 * @author Adam Gibson
 *
 */
public abstract class BaseDataFetcher implements DataSetFetcher {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -859588773699432365L;
	protected int cursor = 0;
	protected int numOutcomes = -1;
	protected int inputColumns = -1;
	protected DataSet curr;
	protected int totalExamples;
	protected static final Logger log = LoggerFactory.getLogger(BaseDataFetcher.class);
	
	/**
	 * Creates a feature vector
	 * @param numRows the number of examples
 	 * @return a feature vector
	 */
	protected INDArray createInputMatrix(int numRows) {
		return Nd4j.create(numRows, inputColumns);
	}
	
	/**
	 * Creates an output label matrix
	 * @param outcomeLabel the outcome label to use
	 * @return a binary vector where 1 is applyTransformToDestination to the
	 * index specified by outcomeLabel
	 */
	protected INDArray createOutputVector(int outcomeLabel) {
		return FeatureUtil.toOutcomeVector(outcomeLabel, numOutcomes);
	}
	
	protected INDArray createOutputMatrix(int numRows) {
		return Nd4j.create(numRows,numOutcomes);
	}
	
	/**
	 * Initializes this data applyTransformToDestination fetcher from the passed in datasets
	 * @param examples the examples to use
	 */
	protected void initializeCurrFromList(List<DataSet> examples) {
		
		if(examples.isEmpty())
			log.warn("Warning: empty dataset from the fetcher");
		curr = null;
		INDArray inputs = createInputMatrix(examples.size());
		INDArray labels = createOutputMatrix(examples.size());
		for(int i = 0; i < examples.size(); i++) {
			inputs.putRow(i, examples.get(i).getFeatureMatrix());
			labels.putRow(i,examples.get(i).getLabels());
		}
		curr = new DataSet(inputs,labels);
        examples.clear();

	}
	
	@Override
	public boolean hasMore() {
		return cursor < totalExamples;
	}

	@Override
	public DataSet next() {
		return curr;
	}

	@Override
	public int totalOutcomes() {
		return numOutcomes;
	}

	@Override
	public int inputColumns() {
		return inputColumns;
	}

	@Override
	public int totalExamples() {
		return totalExamples;
	}

	@Override
	public void reset() {
		cursor = 0;
	}

	@Override
	public int cursor() {
		return cursor;
	}
	
	

	
}
